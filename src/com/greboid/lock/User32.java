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

import com.sun.jna.Callback;
import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

/**
 * Provides access to the w32 user32 library.
 */
interface User32 extends com.sun.jna.platform.win32.User32 {

    /**
     * Creates a window that cannot be activated by the user.
     */
    public static final int WS_EX_NOACTIVATE = 0x08000000;
    /**
     * The window is initially disabled.
     */
    public static final int WS_DISABLED = 0x08000000;
    /**
     * Indicates that the window class is an application global class.
     */
    public static final int CS_GLOBALCLASS = 0x4000;
    /**
     * Pre-existing User32 instance.
     */
    public static final User32 INSTANCE = (User32) Native.loadLibrary("user32",
            User32.class, W32APIOptions.UNICODE_OPTIONS);
    /**
     * Sets a new address for the window procedure.
     */
    public static final int GWLP_WNDPROC = -4;

    /**
     * Replaces a callback of the specified attribute.
     *
     * @param hWnd A handle to the window procedure to receive the message.
     * @param nIndex Value to be set
     * @param callback Replacement callback
     *
     * @return Returns 0 on success, otherwise returns the error value
     */
    public int SetWindowLongPtr(WinDef.HWND hWnd, int nIndex, Callback callback)
            throws LastErrorException;

    /**
     * Replaces a callback of the specified attribute.
     *
     * @param hWnd A handle to the window procedure to receive the message.
     * @param nIndex Value to be set
     * @param callback Replacement callback
     *
     * @return Returns 0 on success, otherwise returns the error value
     */
    public int SetWindowLong(WinDef.HWND hWnd, int nIndex, Callback callback)
            throws LastErrorException;

    /**
     * Replaces a callback of the specified attribute.
     *
     * @param hWnd A handle to the window procedure to receive the message.
     * @param nIndex Value to be set
     * @param callback Replacement callback
     *
     * @return Returns 0 on success, otherwise returns the error value
     */
    public int SetWindowLong(WinDef.HWND hWnd, int nIndex, LONG_PTR callback)
            throws LastErrorException;

    /**
     * Passes message information to the specified window procedure.
     *
     * @param lpPrevWndFunc The previous window procedure.
     * @param hWnd A handle to the window procedure to receive the message.
     * @param Msg The message.
     * @param wParam Additional message-specific information.
     * @param lParam Additional message-specific information.
     *
     * @return
     */
    public WinDef.LRESULT CallWindowProc(LONG_PTR lpPrevWndFunc,
            HWND hWnd, int Msg, WPARAM wParam, LPARAM lParam);

    public HWND CreateWindowEx(final int dwExStyle, final String lpClassName,
            final String lpWindowName, final int dwStyle, final int x,
            final int y, final int nWidth, final int nHeight, final int parent,
            final int hMenu, final int hInstance, final Pointer lpParam);

    public boolean DestroyWindow(HWND hWnd);
}
