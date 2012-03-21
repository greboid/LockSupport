/*
 * Copyright (c) 2006-2012 Greg 'Greboid' Holmes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.greboid.lock;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef.HWND;

import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import lombok.ListenerSupport;

/**
 * Adds support for listening to Window session lock/unlock events.
 */
@ListenerSupport(value = LockListener.class)
public class LockAdapter {

    /**
     * Are we actively listening to session events.
     */
    private boolean listening = false;
    /**
     * Java window to receive message.
     */
    private final Window window;
    /**
     * Native window handle for the java window.
     */
    private HWND hwnd;
    /**
     * Stores the window proc being replaced by this listener.
     */
    private LONG_PTR oldWindowProc;

    /**
     * Adds support to listen to Windows session lock/unlock events.
     *
     * @param window Window to register listener with
     */
    public LockAdapter(final Window window) {
        this.window = window;

        if (window.isDisplayable()) {
            attach();
        }
        window.addComponentListener(new ComponentAdapter() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void componentShown(final ComponentEvent e) {
                if (!listening) {
                    attach();
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void componentHidden(final ComponentEvent e) {
                if (listening) {
                    detach();
                }
            }
        });
    }

    /**
     * Checks the handle to the window exits, creates it if it doesn't exist.
     */
    private void checkHandle() {
        if (hwnd == null) {
            HWND hWnd = new HWND();
            hWnd.setPointer(Native.getWindowPointer(window));
            this.hwnd = hWnd;
        }
    }

    /**
     * Attaches this listener to its window.
     */
    private void attach() {
        checkHandle();
        try {
            oldWindowProc = User32.INSTANCE.GetWindowLongPtr(hwnd,
                    User32.GWL_WNDPROC);
        } catch (UnsatisfiedLinkError ex) {
            oldWindowProc = new LONG_PTR(User32.INSTANCE.GetWindowLong(hwnd,
                    User32.GWL_WNDPROC));
        }
        Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hwnd,
                Wtsapi32.NOTIFY_FOR_THIS_SESSION);
        try {
            User32.INSTANCE.SetWindowLongPtr(hwnd, User32.GWLP_WNDPROC,
                    new WindowProcCallbackImpl(oldWindowProc, this));
        } catch (UnsatisfiedLinkError ex) {
            User32.INSTANCE.SetWindowLong(hwnd, User32.GWLP_WNDPROC,
                    new WindowProcCallbackImpl(oldWindowProc, this));
        }
        listening = true;
    }

    /**
     * Detaches this listener from its window.
     */
    private void detach() {
        checkHandle();
        Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hwnd);
        try {
            User32.INSTANCE.SetWindowLongPtr(hwnd, User32.GWLP_WNDPROC,
                    oldWindowProc);
        } catch (UnsatisfiedLinkError ex) {
            User32.INSTANCE.SetWindowLong(hwnd, User32.GWLP_WNDPROC,
                    oldWindowProc);
        }
        listening = false;
    }
}