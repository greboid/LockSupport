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
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Passes message information to the specified window procedure.
 */
interface WndProcCallback extends StdCallLibrary.StdCallCallback, Callback {

    /**
     * Passes message information to the specified window procedure.
     *
     * @param hWnd A handle to the window procedure to receive the message.
     * @param uMsg The message.
     * @param uParam Additional message-specific information.
     * @param lParam Additional message-specific information.
     *
     * @return The return value specifies the result of the message processing
     * and depends on the message sent.
     */
    LRESULT callback(HWND hWnd, int uMsg, WPARAM uParam, LPARAM lParam);
}
