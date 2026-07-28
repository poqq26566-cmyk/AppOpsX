package com.zzzmode.appopsx.common;

import android.content.pm.PackageManager;
import rikka.shizuku.Shizuku;

// Everything here is defensive: Shizuku may not be installed, its service may not be
// running, or the user may not have granted permission yet. Any of those should just
// mean "Shizuku isn't usable right now", never a crash.
public class ShizukuCompat {

  public static boolean isAvailable() {
    try {
      return Shizuku.pingBinder();
    } catch (Throwable t) {
      return false;
    }
  }

  public static boolean hasPermission() {
    if (!isAvailable()) {
      return false;
    }
    try {
      if (Shizuku.isPreV11()) {
        // Older (pre-API 28 style) permission model isn't handled here.
        return false;
      }
      return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
    } catch (Throwable t) {
      return false;
    }
  }

  // True when Shizuku's service is reachable but we haven't been granted permission
  // yet -- i.e. worth showing a "grant Shizuku permission" prompt for.
  public static boolean needsPermissionRequest() {
    return isAvailable() && !hasPermission() && !Shizuku.isPreV11();
  }

  // Shared helper: opens a Process running with whatever privilege Shizuku itself was
  // started with (root or adb shell). Shizuku.newProcess is marked private in the
  // public API jar, so this goes through reflection -- documented community workaround,
  // see https://github.com/RikkaApps/Shizuku-API/issues/276
  public static Process newProcess(String[] cmd) throws Exception {
    java.lang.reflect.Method m = Shizuku.class.getDeclaredMethod(
        "newProcess", String[].class, String[].class, String.class);
    m.setAccessible(true);
    return (Process) m.invoke(null, (Object) cmd, null, null);
  }
}
