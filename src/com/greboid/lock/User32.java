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
import com.sun.jna.Pointer;
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
     * Creates an overlapped, pop-up, or child window with an extended window
     * style; otherwise, this function is identical to the CreateWindow function.
     *
     * @param dwExStyle The extended window style of the window being created.
     * @param lpClassName Class name previously registered by RegisterClass,
     * RegisterClassEx or a system class name.
     * @param lpWindowName The window name.
     * @param dwStyle The style of the window being created.
     * @param x The initial horizontal position of the window.
     * @param y The initial vertical position of the window.
     * @param nWidth The width, in device units, of the window.
     * @param nHeight The height, in device units, of the window.
     * @param parent A handle to the parent or owner window of the window being
     * created.
     * @param hMenu A handle to a menu, or specifies a child-window identifier,
     * depending on the window style.
     * @param hInstance A handle to the instance of the module to be associated
     * with the window.
     * @param lpParam Pointer to a value to be passed to the window through
     * the CREATESTRUCT structure (lpCreateParams member) pointed to by the
     * lParam param of the WM_CREATE message.
     *
     * @return New window handle or null if call failed
     */
    public HWND CreateWindowEx(int dwExStyle, String lpClassName,
            String lpWindowName, int dwStyle, int x, int y, final int nWidth,
            int nHeight, int parent, int hMenu, int hInstance, Pointer lpParam);

    /**
     * Destroys the specified window.
     *
     * @param hWnd A handle to the window to be destroyed.
     *
     * @return If the function succeeds, the return value is nonzero.
     */
    public boolean DestroyWindow(HWND hWnd);
}
