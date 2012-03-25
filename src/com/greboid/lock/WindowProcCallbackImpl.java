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

import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef;

/**
 * Listener firing window proc callback.
 */
class WindowProcCallbackImpl implements WndProcCallback {

    /**
     * Replaced window proc callback.
     */
    private final LONG_PTR oldWindowProc;
    /**
     * Lock Adapter instance to fire listener on.
     */
    private final LockAdapter lockAdapter;

    /**
     * Creates a new window procedure callback that fires listeners in
     * LockAdapter on session lock and unlock events.
     *
     * @param oldWindowProc Original window procedure callback
     * @param lockAdapter Lock Adapter
     */
    WindowProcCallbackImpl(final LONG_PTR oldWindowProc,
            final LockAdapter lockAdapter) {
        this.oldWindowProc = oldWindowProc;
        this.lockAdapter = lockAdapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WinDef.LRESULT callback(final WinDef.HWND hWnd, final int uMsg,
            final WinDef.WPARAM uParam, final WinDef.LPARAM lParam) {
        if (uMsg == Wtsapi32.WTS_SESSION_CHANGE) {
            if (uParam.intValue() == Wtsapi32.WTS_SESSION_LOCK) {
                lockAdapter.fireLocked();
            } else if (uParam.intValue() == Wtsapi32.WTS_SESSION_UNLOCK) {
                lockAdapter.fireUnlocked();
            }
        }
        return User32.INSTANCE.CallWindowProc(oldWindowProc, hWnd, uMsg,
                uParam, lParam);
    }
}
