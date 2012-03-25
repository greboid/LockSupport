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

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.MSG;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Adds support for listening to Window session lock/unlock events.
 */
public class LockAdapter {

    /**
     * List of lock listeners registered with this adapter.
     */
    private final List<LockListener> listeners = new CopyOnWriteArrayList<LockListener>();
    /**
     * Are we actively listening to session events.
     */
    private boolean listening = false;
    /**
     * Native window handle for the java window.
     */
    private HWND hwnd;

    /**
     * Adds support to listen to Windows session lock/unlock events.
     *
     * @param window Window to register listener with
     */
    public LockAdapter() {
    }

    /**
     * Starts this lock adapter listening for lock events.
     */
    private void start() {
        System.out.println("starting");
        new Thread(new Runnable() {

            @Override
            public void run() {
                attach();
            }
        }).start();
    }

    /**
     * Stops this lock adapter listening for lock events.
     */
    private void stop() {
        listening = false;
        detach();
    }

    /**
     * Checks the handle to the window exits, creates it if it doesn't exist.
     */
    private void checkHandle() {
        if (hwnd == null) {
            hwnd = User32.INSTANCE.CreateWindowEx(User32.WS_EX_NOACTIVATE,
                    "STATIC", "Session Info", User32.WS_DISABLED, 0, 0, 100, 100,
                    0, 0, User32.CS_GLOBALCLASS, null);
        }
    }

    /**
     * Attaches this listener to its window.
     */
    private void attach() {
        checkHandle();

        if (!listening) {
            Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hwnd,
                    Wtsapi32.NOTIFY_FOR_THIS_SESSION);
            listening = true;
        }

        MSG msg = new MSG();
        while (listening && User32.INSTANCE.GetMessage(msg, hwnd, 0, 0) > 0) {
            if (msg.message == Wtsapi32.WTS_SESSION_CHANGE) {
                switch (msg.wParam.intValue()) {
                    case 7:
                        fireLocked();
                        break;
                    case 8:
                        fireUnlocked();
                        break;
                    default:
                        break;
                }
            }
            User32.INSTANCE.TranslateMessage(msg);
            User32.INSTANCE.DispatchMessage(msg);
        }
    }

    public static void main(final String... args) throws IOException {
        LockAdapter la = new LockAdapter();
        la.addLockListener(new LockListener() {

            @Override
            public void locked() {
                System.out.println("locked");
            }

            @Override
            public void unlocked() {
                System.out.println("unlocked");
            }
        });
        System.in.read();
    }

    /**
     * Detaches this listener from its window.
     */
    private void detach() {
        checkHandle();
        Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hwnd);
        User32.INSTANCE.DestroyWindow(hwnd);
        hwnd = null;
        listening = false;
    }

    /**
     * Adds the specified listener to this adapter.
     *
     * @param l Lock listener
     */
    public void addLockListener(final LockListener l) {
        System.out.println("adding listener");
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
        if (!listeners.isEmpty()) {
            start();
        }
    }

    /**
     * Removes the specified listener from this adapter.
     *
     * @param l Lock listener
     */
    public void removeLockListener(final LockListener l) {
        listeners.remove(l);
        if (listeners.isEmpty()) {
            stop();
        }
    }

    /**
     * Triggers locked method on all listeners.
     */
    void fireLocked() {
        System.out.println("firing lock");
        for (LockListener l : listeners) {
            l.locked();
        }
    }

    /**
     * Triggers unlocked method on all listeners.
     */
    void fireUnlocked() {
        System.out.println("firing unlock");
        for (LockListener l : listeners) {
            l.unlocked();
        }
    }
}