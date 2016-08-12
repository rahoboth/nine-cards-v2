package com.fortysevendeg.ninecardslauncher.app.ui.commons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable._
import android.os.Vibrator
import android.view.{View, ViewGroup}
import ColorOps._
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.text.SpannableString
import android.text.style.UnderlineSpan
import com.fortysevendeg.ninecardslauncher.app.ui.commons.AppUtils._
import android.view.inputmethod.InputMethodManager
import android.widget.{EditText, ImageView, Spinner, TextView}
import com.fortysevendeg.macroid.extras.DeviceVersion.Lollipop
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ViewOps._
import com.fortysevendeg.ninecardslauncher.app.ui.components.drawables.DrawerBackgroundDrawable
import com.fortysevendeg.ninecardslauncher.commons._
import com.fortysevendeg.ninecardslauncher2.R
import macroid._

object CommonsTweak {

  def vBackgroundBoxWorkspace(color: Int, horizontalPadding: Int = 0, verticalPadding: Int = 0)(implicit contextWrapper: ContextWrapper): Tweak[View] = {
    val radius = resGetDimensionPixelSize(R.dimen.radius_default)
    Lollipop.ifSupportedThen {
      vBackgroundColor(color) +
        vClipBackground(radius, horizontalPadding = horizontalPadding, verticalPadding = verticalPadding) +
        vElevation(resGetDimensionPixelSize(R.dimen.elevation_box_workspaces))
    } getOrElse {
      val drawable = new DrawerBackgroundDrawable(color, horizontalPadding, verticalPadding, radius)
      vBackground(drawable)
    }
  }

  def vBackgroundCollection(indexColor: Int)(implicit contextWrapper: ContextWrapper): Tweak[View] =
    vBackgroundCircle(resGetColor(getIndexColor(indexColor)))

  def vBackgroundCircle(color: Int)(implicit contextWrapper: ContextWrapper): Tweak[View] = {
    def createShapeDrawable(c: Int) = {
      val drawableColor = new ShapeDrawable(new OvalShape())
      drawableColor.getPaint.setColor(c)
      drawableColor.getPaint.setStyle(Paint.Style.FILL)
      drawableColor.getPaint.setAntiAlias(true)
      drawableColor
    }

    def getDrawable(c: Int): Drawable = {
      val drawableColor = createShapeDrawable(c)
      val padding = resGetDimensionPixelSize(R.dimen.elevation_default)
      val drawableShadow = createShapeDrawable(resGetColor(R.color.shadow_default))
      val layer = new LayerDrawable(Array(drawableShadow, drawableColor))
      layer.setLayerInset(0, padding, padding, padding, 0)
      layer.setLayerInset(1, padding, 0, padding, padding)
      layer
    }
    val elevation = resGetDimensionPixelSize(R.dimen.elevation_default)

    vBackground(Lollipop ifSupportedThen {
      new RippleDrawable(
        new ColorStateList(Array(Array()), Array(color.dark(0.2f))),
        createShapeDrawable(color),
        javaNull)
    } getOrElse {
      val states = new StateListDrawable()
      states.addState(Array[Int](android.R.attr.state_pressed), getDrawable(color.dark()))
      states.addState(Array.emptyIntArray, getDrawable(color))
      states
    }) + (Lollipop ifSupportedThen vElevation(elevation) getOrElse Tweak.blank)
  }

  def vSetPosition(position: Int): Tweak[View] = vTag(R.id.position, position)

  def vSetType(t: String) = vTag(R.id.view_type, t)

  def vAddField[T](key: String, value: T) = Tweak[View] { view =>
    view.setTag(R.id.fields_map, view.getFieldsMap + ((key, value)))
  }

  def vRemoveField(key: String) = Tweak[View] { view =>
    view.setTag(R.id.fields_map, view.getFieldsMap - key)
  }

  def vUseLayerHardware = vTag(R.id.use_layer_hardware, "")

  def vLayerHardware(activate: Boolean) = Transformer {
    case v: View if v.hasLayerHardware => v <~ (if (activate) vLayerTypeHardware() else vLayerTypeNone())
  }

}

object ExtraTweaks {

  // TODO - Move to macroid extras

  def vgAddViewByIndexParams[V <: View](view: V, index: Int, params: ViewGroup.LayoutParams): Tweak[ViewGroup] =
    Tweak[ViewGroup](_.addView(view, index, params))

  def ivBlank: Tweak[ImageView] = Tweak[ImageView](_.setImageBitmap(javaNull))

  def vDisableHapticFeedback: Tweak[View] = Tweak[View](_.setHapticFeedbackEnabled(false))

  def uiVibrate(millis: Long = 100)(implicit contextWrapper: ContextWrapper): Ui[Any] = Ui {
    contextWrapper.application.getSystemService(Context.VIBRATOR_SERVICE) match {
      case vibrator: Vibrator => vibrator.vibrate(millis)
      case _ =>
    }
  }

  def etShowKeyboard(implicit contextWrapper: ContextWrapper) = Tweak[EditText] { editText =>
    editText.requestFocus()
    Option(contextWrapper.bestAvailable.getSystemService(Context.INPUT_METHOD_SERVICE)) foreach {
      case imm: InputMethodManager => imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
      case _ =>
    }
  }

  def dlOpenDrawerEnd: Tweak[DrawerLayout] = Tweak[DrawerLayout](_.openDrawer(GravityCompat.END))

  def dlCloseDrawerEnd: Tweak[DrawerLayout] = Tweak[DrawerLayout](_.closeDrawer(GravityCompat.END))

  def dlLockedClosed: Tweak[DrawerLayout] = Tweak[DrawerLayout](_.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED))

  def dlUnlocked: Tweak[DrawerLayout] = Tweak[DrawerLayout](_.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED))

  def dlLockedOpen: Tweak[DrawerLayout] = Tweak[DrawerLayout](_.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN))

  def dlSwapDrawer: Tweak[DrawerLayout] = Tweak[DrawerLayout] { view =>
    if (view.isDrawerOpen(GravityCompat.START)) {
      view.closeDrawer(GravityCompat.START)
    } else {
      view.openDrawer(GravityCompat.START)
    }
  }

  def dlSwapDrawerEnd: Tweak[DrawerLayout] = Tweak[DrawerLayout] { view =>
    if (view.isDrawerOpen(GravityCompat.END)) {
      view.closeDrawer(GravityCompat.END)
    } else {
      view.openDrawer(GravityCompat.END)
    }
  }

  def tvUnderlineText(text: String): Tweak[TextView] = Tweak[TextView] { tv =>
    val content = new SpannableString(text)
    content.setSpan(new UnderlineSpan(), 0, text.length, 0)
    tv.setText(content)
  }

  def sSelection(position: Int) = Tweak[Spinner](_.setSelection(position))

  def tvHintColor(color: Int): Tweak[TextView] = Tweak[TextView](_.setHintTextColor(color))

}