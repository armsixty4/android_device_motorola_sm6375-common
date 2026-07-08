/*
 * Copyright (C) 2023 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.motorola.parts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Display;
import android.view.Display.HdrCapabilities;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "MotoParts";
    private static final boolean DEBUG = true;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }
        if (DEBUG)
            Log.d(TAG, "Received boot completed intent");

        try {
            DisplayManager dm = context.getSystemService(DisplayManager.class);
            int[] hdrTypes = new int[] {
                    HdrCapabilities.HDR_TYPE_DOLBY_VISION,
                    HdrCapabilities.HDR_TYPE_HDR10,
                    HdrCapabilities.HDR_TYPE_HLG,
                    HdrCapabilities.HDR_TYPE_HDR10_PLUS,
            };
            dm.overrideHdrTypes(Display.DEFAULT_DISPLAY, hdrTypes);

            if (DEBUG)
                Log.d(TAG, "HDR types overridden successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to override HDR types", e);
        }
    }
}
