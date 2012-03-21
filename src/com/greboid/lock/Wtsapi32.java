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
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Provides access to the w32 Wtsapi32 library.
 */
public interface Wtsapi32 extends StdCallLibrary {

    /**
     * All session notifications are to be received.
     */
    public static final int NOTIFY_FOR_ALL_SESSIONS = 1;
    /**
     * Only session notifications involving the session attached to by the
     * window identified by the hWnd parameter value are to be received.
     */
    public static final int NOTIFY_FOR_THIS_SESSION = 0;
    /**
     * A session change event has occurred.
     */
    public static final int WTS_SESSION_CHANGE = 0x2b1;
    /**
     * The session has been locked.
     */
    public static final int WTS_SESSION_LOCK = 7;
    /**
     * The session has been unlocked.
     */
    public static final int WTS_SESSION_UNLOCK = 8;
    /**
     * Pre-existing Wtsapi32 instance.
     */
    Wtsapi32 INSTANCE = (Wtsapi32) Native.loadLibrary("Wtsapi32",
            Wtsapi32.class, W32APIOptions.UNICODE_OPTIONS);

    /**
     * Registers the specified window to receive session change notifications.
     *
     * @param hWnd Handle of the window to receive session change notifications.
     * @param dwFlags Specifies which session notifications are to be received.
     *
     * @return true on success, false otherwise
     */
    public boolean WTSRegisterSessionNotification(final HWND hWnd,
            final int dwFlags);

    /**
     * Unregisters the specified window so that it receives no further session
     * change notifications.
     *
     * @param hWnd Handle of the window to be unregistered from receiving
     * session notifications.
     *
     * @return true on success, false otherwise
     */
    public boolean WTSUnRegisterSessionNotification(final HWND hWnd);
}
