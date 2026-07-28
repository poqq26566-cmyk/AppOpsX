package com.zzzmode.appopsx.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.zzzmode.appopsx.ui.core.LangHelper;
import com.zzzmode.appopsx.ui.core.SpHelper;

/**
 * Created by zl on 2017/1/7.
 */

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    LangHelper.updateLanguage(this);
    super.onCreate(savedInstanceState);
    AppCompatDelegate.setDefaultNightMode(SpHelper.getThemeMode(this));

    // Android 15+ (targetSdk 35+) draws content edge-to-edge by default, so the toolbar
    // otherwise ends up underneath the status bar unless we pad for it ourselves. Doing
    // this once here (rather than per-activity layout) covers every screen in the app.
    WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
  }

  private void applyStatusBarInsets() {
    View content = findViewById(android.R.id.content);
    if (content == null) return;

    ViewCompat.setOnApplyWindowInsetsListener(content, (v, windowInsets) -> {
      Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(v.getPaddingLeft(), bars.top, v.getPaddingRight(), v.getPaddingBottom());
      return windowInsets;
    });
  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    applyStatusBarInsets();
  }

  @Override
  public void setContentView(View view) {
    super.setContentView(view);
    applyStatusBarInsets();
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(LangHelper.attachBaseContext(newBase));
  }

}
