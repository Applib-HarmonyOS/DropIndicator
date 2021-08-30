package comulez.github.dropindicator;

import static ohos.agp.render.BlendMode.LIGHTEN;
import static ohos.multimodalinput.event.TouchEvent.PRIMARY_POINT_DOWN;

import java.util.logging.Level;
import java.util.logging.Logger;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSlider;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

/**Created by Ulez on 2016/11/24.
 * Email：lcy1532110757@gmail.com.
 * Class handling the feature of adding drops and thier animation.
 */
public class DropIndicator extends ComponentContainer
        implements Component.DrawTask, Component.EstimateSizeListener,
        Component.TouchEventListener, Component.LayoutRefreshedListener {

  private static final float DEFRADIUS = 42f;
  private static final int DEFDURATION = 1000;
  private static final float DEFSCALE = 0.8f;
  private static final double CONST = 0.552284749831;
  private static final double GCONST = 1.41421;
  private static final int RCONST = 1;
  private boolean direction = true;
  private float lastCurrentTime = 0;
  private Color[] roundColors = new Color[4];
  private Color startColor;
  private Color endColor;
  private float radius;
  private float mcConst;
  private int duration;
  private float scale;
  private Paint clickPaint;
  private Paint paintCircle;
  private Paint mpaint;
  private float ratio = 50;
  private PageSlider.PageChangedListener pgChangedListener;
  private float startX;
  private int startY;
  private boolean needInit = false;
  private AnimatorValue animator;
  private PageSlider mviewPager;
  private boolean animating;
  private float currentTime;
  private int tabNum = 0;
  private Xpoint pt2;
  private Xpoint pt4;
  private Ypoint pt1;
  private Ypoint pt3;
  private float div;
  private float distance;
  private int currentPos = 0;
  private int toPos = -1;
  private Path mpath = new Path();
  private boolean isSwipe = false;
  private float[][] colourHolder;
  private float[] result;
  public static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "LIBTEST");
  private static final Logger LOGGER = Logger.getLogger("java.util.logger");

  public DropIndicator(final Context context) {
    super(context);
  }

  public DropIndicator(final Context context, final AttrSet attrSet) {
    this(context, attrSet, null);
  }

  /** A constructor to initialize default values.
   *
   * @param context app context
   * @param attrSet xml attributes
   * @param styleName style
   */
  public DropIndicator(final Context context, final AttrSet attrSet, final String styleName) {
    super(context, attrSet, styleName);
    final int defaultColor1 = RgbPalette.parse("#FCC04D");
    roundColors[0] = new Color(defaultColor1);
    final int defaultColor2 = RgbPalette.parse("#00C3E2");
    roundColors[1] = new Color(defaultColor2);
    final int defaultColor3 = RgbPalette.parse("#FE626D");
    roundColors[2] = new Color(defaultColor3);
    final int defaultColor4 = RgbPalette.parse("#966ACF");
    roundColors[3] = new Color(defaultColor4);
    Color clickColor = Color.WHITE;
    Color circleColor = Color.GRAY;
    radius = DEFRADIUS;
    duration = DEFDURATION;
    scale = DEFSCALE;

    final String dpColor1 = "color1";
    if (attrSet.getAttr(dpColor1).isPresent()) {
      roundColors[0] = attrSet.getAttr(dpColor1).get().getColorValue();
    }
    final String dpColor2 = "color2";
    if (attrSet.getAttr(dpColor2).isPresent()) {
      roundColors[1] = attrSet.getAttr(dpColor2).get().getColorValue();
    }
    final String dpColor3 = "color3";
    if (attrSet.getAttr(dpColor3).isPresent()) {
      roundColors[2] = attrSet.getAttr(dpColor3).get().getColorValue();
    }
    final String dpColor4 = "color4";
    if (attrSet.getAttr(dpColor4).isPresent()) {
      roundColors[3] = attrSet.getAttr(dpColor4).get().getColorValue();
    }
    final String dpClickColor = "click_color";
    if (attrSet.getAttr(dpClickColor).isPresent()) {
      clickColor = attrSet.getAttr(dpClickColor).get().getColorValue();
    }
    final String dpCircleColor = "circle_color";
    if (attrSet.getAttr(dpCircleColor).isPresent()) {
      circleColor = attrSet.getAttr(dpCircleColor).get().getColorValue();
    }
    final String dpRadius = "radius";
    if (attrSet.getAttr(dpRadius).isPresent()) {
      radius = attrSet.getAttr(dpRadius).get().getDimensionValue();
    }
    final String dpDuration = "duration";
    if (attrSet.getAttr(dpDuration).isPresent()) {
      duration = attrSet.getAttr(dpDuration).get().getIntegerValue();
    }
    final String dpScale = "scale";
    if (attrSet.getAttr(dpScale).isPresent()) {
      scale = attrSet.getAttr(dpScale).get().getFloatValue();
    }
    colourHolder = new float[roundColors.length][3];
    result = new float[3];
    ratio = radius;
    mcConst = (float) (CONST * ratio);

    paintCircle = new Paint();
    paintCircle.setColor(circleColor);
    paintCircle.setStyle(Paint.Style.STROKE_STYLE);
    paintCircle.setAntiAlias(true);
    paintCircle.setStrokeWidth(3);

    clickPaint = new Paint();
    clickPaint.setColor(clickColor);
    clickPaint.setAntiAlias(true);
    clickPaint.setStrokeWidth(radius / 2);

    mpaint = new Paint();
    startColor = roundColors[0];
    mpaint.setColor(startColor);
    mpaint.setStyle(Paint.Style.FILL_STYLE);
    mpaint.setStrokeWidth(1);
    mpaint.setAntiAlias(true);
    mpaint.setBlendMode(LIGHTEN);
    needInit = true;
    setEstimateSizeListener(this);
    setLayoutRefreshedListener(this);
    setTouchEventListener(this);
    addDrawTask(this);
  }

  private void init() {
    tabNum = getChildCount();
    final int width = getWidth();
    final int height = getHeight();
    div = (width - 2 * tabNum * radius) / (tabNum + 1);
    startX = div + radius;
    startY = height / 2;
    if (currentPos == 0) {
      radius = RCONST * ratio;
      mcConst = (float) (CONST * ratio);
      pt1 = new Ypoint(0, radius, mcConst);
      pt3 = new Ypoint(0, -radius, mcConst);
      pt2 = new Xpoint(radius, 0, mcConst);
      pt4 = new Xpoint(-radius, 0, mcConst);
    }
    needInit = false;
  }

  private void startAniTo(final int currentPos, final int toPos) {
    if (animator != null) {
      animator.cancel();
    }
    this.currentPos = currentPos;
    this.toPos = toPos;
    if (currentPos != toPos) {
      startColor = roundColors[(this.currentPos) % 4];
      endColor = roundColors[(toPos) % 4];
      resetP();
      isSwipe = false;
      startX = div + radius + (this.currentPos) * (div + 2 * radius);
      distance = (toPos - this.currentPos) * (2 * radius + div)
              + (toPos > currentPos ? -radius : radius);

      if (animator == null) {
        animator = new AnimatorValue();
        animator.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
        animator.setDuration(duration);
        animator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
          @Override
          public void onUpdate(final AnimatorValue animatorValue, final  float value) {
            currentTime = value;
            invalidate();
          }
        });
        animator.setStateChangedListener(new Animator.StateChangedListener() {
          @Override
          public void onStart(final Animator animator) {
            animating = true;
            setTouchAble(false);
          }

          @Override
          public void onStop(final Animator animator) {
            //No operation.
          }

          @Override
          public void onCancel(final Animator animator) {
            goo();
            animating = false;
            setTouchAble(true);
            currentTime = 1;
            invalidate();
          }

          @Override
          public void onEnd(final Animator animator) {
            goo();
            animating = false;
            setTouchAble(true);
          }

          @Override
          public void onPause(final Animator animator) {
            //No operation.
          }

          @Override
          public void onResume(final Animator animator) {
            //No operation.
          }
        });
      }
      animator.start();
      if (mviewPager != null) {
        mviewPager.setCurrentPage(toPos);
      }
    }
  }

  @Override
  public void onDraw(final Component component, final Canvas canvas) {
    initDraw(canvas);
    setCanvas(canvas);
    drawCanvas(canvas);
  }

  /** Canvas onDraw.
   *
   * @param canvas canvas object
   */
  public void initDraw(final Canvas canvas) {
    if (needInit) {
      init();
    }
    canvas.save();
    mpath.reset();
    tabNum = getChildCount();
    for (int i = 0; i < tabNum; i++) {
      canvas.drawCircle(div + radius + i * (div + 2 * radius), startY, radius, paintCircle);
    }
    if (currentTime == 0) {
      resetP();
      if (!isSwipe) {
        canvas.drawCircle(div + radius + (currentPos) * (div + 2 * radius), startY, 0,
            clickPaint);
      }
      mpaint.setColor(startColor);
      clickPaint.setStyle(Paint.Style.STROKE_STYLE);
      canvas.translate(startX, startY);
      if (toPos > currentPos) {
        pt2.setX(radius);
      } else {
        pt4.setX(-radius);
      }
    }
  }

  /** Set cnanvas for different time stamp.
   *
   * @param canvas canvas object
   */
  public void setCanvas(final Canvas canvas) {
    if (currentTime > 0 && currentTime <= 0.2) {
      direction = toPos > currentPos;
      if (animating) {
        canvas.drawCircle(div + radius + (toPos) * (div + 2 * radius), startY,
            radius * 1.0f * 5 * currentTime, clickPaint);
      }
      canvas.translate(startX, startY);
      setCoordinates(canvas, TimeRange.TIME_0_2);
    } else if (currentTime > 0.2 && currentTime <= 0.5) {
      canvas.translate(startX + (currentTime - 0.2f) * distance / 0.7f, startY);
      setCoordinates(canvas, TimeRange.TIME_2_5);
    } else if (currentTime > 0.5 && currentTime <= 0.8) {
      canvas.translate(startX + (currentTime - 0.2f) * distance / 0.7f, startY);
      setCoordinates(canvas, TimeRange.TIME_5_8);
    } else if (currentTime > 0.8 && currentTime <= 0.9) {
      pt2.setMc(mcConst);
      pt4.setMc(mcConst);
      canvas.translate(startX + (currentTime - 0.2f) * distance / 0.7f, startY);
      setCoordinates(canvas, TimeRange.TIME_8_9);
    } else if (currentTime > 0.9 && currentTime < 1) {
      setCoordinates(canvas, TimeRange.TIME_9_1);
    }
  }

  private enum TimeRange {
    TIME_0_2, TIME_2_5, TIME_5_8, TIME_8_9, TIME_9_1
  }

  /** Set coordinates for different time stamp.
   *
   * @param canvas canvas object
   */
  public void setCoordinates(final Canvas canvas, final TimeRange timeRange) {
    if (toPos > currentPos) {
      switch (timeRange) {
        case TIME_0_2:
          pt2.setX(radius + 2 * 5 * currentTime * radius / 2);
          break;
        case TIME_2_5:
          pt2.setX(2 * radius);
          pt1.setX(0.5f * radius * (currentTime - 0.2f) / 0.3f);
          pt3.setX(0.5f * radius * (currentTime - 0.2f) / 0.3f);
          pt2.setMc(mcConst + (currentTime - 0.2f) * mcConst / 4 / 0.3f);
          pt4.setMc(mcConst + (currentTime - 0.2f) * mcConst / 4 / 0.3f);
          break;
        case TIME_5_8:
          pt1.setX(0.5f * radius + 0.5f * radius * (currentTime - 0.5f) / 0.3f);
          pt3.setX(0.5f * radius + 0.5f * radius * (currentTime - 0.5f) / 0.3f);
          pt2.setMc(1.25f * mcConst - 0.25f * mcConst * (currentTime - 0.5f) / 0.3f);
          pt4.setMc(1.25f * mcConst - 0.25f * mcConst * (currentTime - 0.5f) / 0.3f);
          break;
        case TIME_8_9:
          pt4.setX(-radius + 1.6f * radius * (currentTime - 0.8f) / 0.1f);
          break;
        case TIME_9_1:
          pt1.setX(radius);
          pt3.setX(radius);
          canvas.translate(startX + distance, startY);
          pt4.setX(0.6f * radius - 0.6f * radius * (currentTime - 0.9f) / 0.1f);
          break;
        default:
          break;
      }
    } else {
      switch (timeRange) {
        case TIME_0_2:
          pt4.setX(-radius - 2 * 5 * currentTime * radius / 2);
          break;
        case TIME_2_5:
          pt4.setX(-2 * radius);
          pt1.setX(-0.5f * radius * (currentTime - 0.2f) / 0.3f);
          pt3.setX(-0.5f * radius * (currentTime - 0.2f) / 0.3f);
          pt2.setMc(mcConst + (currentTime - 0.2f) * mcConst / 4 / 0.3f);
          pt4.setMc(mcConst + (currentTime - 0.2f) * mcConst / 4 / 0.3f);
          break;
        case TIME_5_8:
          pt1.setX(-0.5f * radius - 0.5f * radius * (currentTime - 0.5f) / 0.3f);
          pt3.setX(-0.5f * radius - 0.5f * radius * (currentTime - 0.5f) / 0.3f);
          pt2.setMc(1.25f * mcConst - 0.25f * mcConst * (currentTime - 0.5f) / 0.3f);
          pt4.setMc(1.25f * mcConst - 0.25f * mcConst * (currentTime - 0.5f) / 0.3f);
          break;
        case TIME_8_9:
          pt2.setX(radius - 1.6f * radius * (currentTime - 0.8f) / 0.1f);
          break;
        case TIME_9_1:
          pt1.setX(-radius);
          pt3.setX(-radius);
          canvas.translate(startX + distance, startY);
          pt2.setX(-0.6f * radius + 0.6f * radius * (currentTime - 0.9f) / 0.1f);
          break;
        default:
          break;
      }
    }
  }

  /** Draw canvas for set coordinates.
   *
   * @param canvas canvas object
   */
  public void drawCanvas(final Canvas canvas) {
    if (currentTime == 1) {
      lastCurrentTime = 0;
      mpaint.setColor(endColor);
      if (direction) {
        pt1.setX(radius);
        pt3.setX(radius);
        canvas.translate(startX + distance, startY);
        pt4.setX(0);
      } else {
        pt1.setX(-radius);
        pt3.setX(-radius);
        canvas.translate(startX + distance, startY);
        pt2.setX(0);
      }
      currentPos = toPos;
      resetP();
      if (direction) {
        canvas.translate(radius, 0);
      } else {
        canvas.translate(-radius, 0);
      }
    }
    mpath.moveTo(pt1.xpt, pt1.ypt);
    mpath.cubicTo(pt1.right.getXpt(), pt1.right.getYpt(), pt2.bottom.getXpt(), pt2.bottom.getYpt(),
        pt2.xpt, pt2.ypt);
    mpath.cubicTo(pt2.top.getXpt(), pt2.top.getYpt(), pt3.right.getXpt(), pt3.right.getYpt(),
        pt3.xpt, pt3.ypt);
    mpath.cubicTo(pt3.left.getXpt(), pt3.left.getYpt(), pt4.top.getXpt(), pt4.top.getYpt(),
        pt4.xpt, pt4.ypt);
    mpath.cubicTo(pt4.bottom.getXpt(), pt4.bottom.getYpt(), pt1.left.getXpt(), pt1.left.getYpt(),
        pt1.xpt, pt1.ypt);
    if (currentTime > 0 && currentTime < 1) {
      mpaint.setColor(getCurrentColor(currentTime, startColor.getValue(), endColor.getValue()));
    }
    canvas.drawPath(mpath, mpaint);
    canvas.restore();
  }

  @Override
  public boolean onEstimateSize(final int widthEstConfig, final int heightEstConfig) {
    // Notify child components in the container component to perform measurement.
    measureChildren(widthEstConfig, heightEstConfig);
    final int maxWidth = 0;
    final int maxHeight = 0;
    setEstimatedSize(
            Component.EstimateSpec.getChildSizeWithMode(maxWidth, widthEstConfig, 0),
            Component.EstimateSpec.getChildSizeWithMode(maxHeight, heightEstConfig, 0));

    return true;
  }

  private void measureChildren(final int widthEstConfig, final int heightEstConfig) {
    for (int idx = 0; idx < getChildCount(); idx++) {
      final Component childView = getComponentAt(idx);
      if (childView != null) {
        measureChild(childView, widthEstConfig, heightEstConfig);
      }
    }
  }

  private void measureChild(final Component child, final int parentWidthSpec,
                            final int parentHeightSpec) {
    final ComponentContainer.LayoutConfig layoutConfig = child.getLayoutConfig();
    final int childWidthSpec = EstimateSpec.getChildSizeWithMode(
            layoutConfig.width, parentWidthSpec, EstimateSpec.UNCONSTRAINT);
    final int childHeightSpec = EstimateSpec.getChildSizeWithMode(
            layoutConfig.height, parentHeightSpec, EstimateSpec.UNCONSTRAINT);
    child.estimateSize(childWidthSpec, childHeightSpec);
  }

  private void onLayout() {
    tabNum = getChildCount();
    for (int j = 0; j < tabNum; j++) {
      final Component child = getComponentAt(j);
      child.setComponentPosition(
              (int) (div + (1 - scale * 1 / GCONST) * radius + j * (div + 2 * radius)),
              (int) (startY - scale * radius / GCONST),
              (int) (div + (1 + scale * 1 / GCONST) * radius + j * (div + 2 * radius)),
              (int) (startY + scale * radius / GCONST));
    }
  }

  @Override
  public boolean onTouchEvent(final Component component, final TouchEvent touchEvent) {
    boolean res = false;
    if (touchEvent.getAction() == PRIMARY_POINT_DOWN) {
      final float xval = touchEvent.getPointerScreenPosition(0).getX();
      if (xval > div + 2 * radius && xval < (div + 2 * radius) * tabNum) {
        final int mtoPos = (int) (xval / (div + 2 * radius));
        if (mtoPos != currentPos && mtoPos <= tabNum) {
          startAniTo(currentPos, mtoPos);
        }
      } else if (xval > div && xval < div + 2 * radius && currentPos != 0) {
        startAniTo(currentPos, 0);
      }
      res = true;
    }
    return res;
  }

  @Override
  public void onRefreshed(final Component component) {
    onLayout();
  }

  /** Method to ignore touch events on page slider.
   *
   * @param touchAble isTouchable
   */
  private void setTouchAble(final boolean touchAble) {
    if (mviewPager instanceof Touchable) {
      ((Touchable) mviewPager).setTouchable(touchAble);
    }
  }

  /** Method to set view pager and handle animation from when view page is moved.
   *
   * @param viewPager View pager object
   */
  public void setViewPager(final PageSlider viewPager) {
    this.mviewPager = viewPager;
    viewPager.addPageChangedListener(new PageSlider.PageChangedListener() {
      @Override
      public void onPageSliding(final int itemPos, final float itemPosOff,
                                final int itemPosOffPix) {
        try {
          if (!animating) {
            updateDrop(itemPos, itemPosOff, itemPosOffPix);
          }
        } catch (Exception e) {
          if (LOGGER.isLoggable(Level.INFO)) {
            HiLog.info(LABEL_LOG,  "Drop indicator - Page sliding error "
                + e.getLocalizedMessage());
          }
        }
        if (pgChangedListener != null) {
          pgChangedListener.onPageSliding(itemPos, itemPosOff, itemPosOffPix);
        }
      }

      @Override
      public void onPageSlideStateChanged(final int state) {
        if (pgChangedListener != null) {
          pgChangedListener.onPageSlideStateChanged(state);
        }
      }

      @Override
      public void onPageChosen(final int itemPos) {
        // No operation.
      }
    });

  }

  private void updateDrop(final int position, final float positionOffset,
                          final int positionOffPix) {
    if (animator != null) {
      animator.cancel();
    }
    isSwipe = true;
    if (positionOffPix > 0) {
      direction = true;
    } else if (positionOffPix < 0) {
      direction = false;
    }

    if (direction) {
      toPos = currentPos + 1;
    } else {
      toPos = currentPos - 1;
    }

    startColor = roundColors[(currentPos) % 4];
    endColor = roundColors[(currentPos + (direction ? 1 : -1)) % 4];
    startX = div + radius + (currentPos) * (div + 2 * radius);
//    distance = direction ? 2 * radius + div - radius :
//            -(2 * radius + div) + radius;
    distance = direction ? ((2 * radius + div) + (direction ? -radius : radius)) :
        (-(2 * radius + div) + (direction ? -radius : radius));
    currentTime = position + positionOffset - (int) (position + positionOffset);

    if (Math.abs(lastCurrentTime - currentTime) > 0.2) {
      if (lastCurrentTime < 0.1) {
        currentTime = 0;
      } else if (lastCurrentTime > 0.9) {
        currentTime = 1;
      }
    }
    HiLog.info(LABEL_LOG, "updateDrop - mCurrentTime "+currentTime +" lastCurrentTime "+lastCurrentTime   );
    HiLog.info(LABEL_LOG, "updateDrop startX "+startX +"distance "+distance +"direction "+direction  );
    lastCurrentTime = currentTime;
    invalidate();
  }

  private void goo() {
    currentPos = toPos;
  }

  private void resetP() {
    pt1.setY(radius);
    pt1.setX(0);
    pt1.setMc(mcConst);

    pt3.setY(-radius);
    pt3.setX(0);
    pt3.setMc(mcConst);

    pt2.setY(0);
    pt2.setX(radius);
    pt2.setMc(mcConst);

    pt4.setY(0);
    pt4.setX(-radius);
    pt4.setMc(mcConst);
  }

  private Color getCurrentColor(final float percent, final int startColor, final int endColor) {
    int[] colors = new int[4];
    colors[0] = startColor;
    colors[1] = RgbPalette.GRAY.asArgbInt();
    colors[2] = RgbPalette.GRAY.asArgbInt();
    colors[3] = endColor;
    for (int i = 0; i < colors.length; i++) {
      colourHolder[i][0] = (colors[i] & 0xff0000) >> 16;
      colourHolder[i][1] = (colors[i] & 0x00ff00) >> 8;
      colourHolder[i][2] = colors[i] & 0x0000ff;
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < colourHolder.length; j++) {
        if (colourHolder.length == 1 || percent == j / (colourHolder.length - 1f)) {
          result = colourHolder[j];
        } else {
          if (percent > j / (colourHolder.length - 1f)
                  && percent < (j + 1f) / (colourHolder.length - 1)) {
            result[i] = colourHolder[j][i] - (colourHolder[j][i] - colourHolder[j + 1][i])
                    * (percent - j / (colourHolder.length - 1f)) * (colourHolder.length - 1f);
          }
        }
      }
    }
    return new Color(Color.rgb((int) result[0], (int) result[1], (int) result[2]));
  }

  /**Point holds two float coordinates.
   */
  public static class Xpoint {
    private float xpt;
    private float ypt;
    private float mcXpt;
    private PointF bottom;
    private PointF top;

    /** Constructor.
     *
     * @param xptVal x coordinate
     * @param yptVal y coordinate
     * @param mcVal mc constant
     */
    public Xpoint(final float xptVal, final float yptVal, final float mcVal) {
      this.xpt = xptVal;
      this.ypt = yptVal;
      this.mcXpt = mcVal;
      if (bottom == null) {
        bottom = new PointF();
      }
      if (top == null) {
        top = new PointF();
      }
      bottom.setYpt(xpt + mcXpt);
      top.setYpt(ypt - mcXpt);
      bottom.setXpt(xpt);
      top.setXpt(xpt);
    }

    /** Method to set mc value.
     *
     * @param mcXpoint mc value for xpoint
     */
    public void setMc(final float mcXpoint) {
      this.mcXpt = mcXpoint;
      bottom.setYpt(ypt + mcXpt);
      top.setYpt(ypt - mcXpt);
    }

    /** Method to set y coordinate.
     *
     * @param ypoint y coordinate
     */
    public void setY(final float ypoint) {
      this.ypt = ypoint;
      bottom.setYpt(ypt + mcXpt);
      top.setYpt(ypt - mcXpt);
    }

    /** Method to set x coordinate.
     *
     * @param xpoint x coordinate
     */
    public void setX(final float xpoint) {
      this.xpt = xpoint;
      bottom.setXpt(xpoint);
      top.setXpt(xpoint);
    }

    @Override
    public String toString() {
      return "XPoint{" + "x=" + xpt + ", y=" + ypt + ", mc=" + mcXpt
              + ", bottom=" + bottom + ", top=" + top + '}';
    }
  }

  /**Point holds two float coordinates.
   */
  public static class Ypoint {
    private float xpt;
    private float ypt;
    private float mcYpt;
    private PointF left;
    private PointF right;

    /** Constructor.
     *
     * @param xptVal x coordinate
     * @param yptVal y coordinate
     * @param mcVal mc constant
     */
    public Ypoint(final float xptVal, final float yptVal, final float mcVal) {
      this.xpt = xptVal;
      this.ypt = yptVal;
      this.mcYpt = mcVal;
      if (left == null) {
        left = new PointF();
      }
      if (right == null) {
        right = new PointF();
      }
      right.setXpt(xpt + mcYpt);
      left.setXpt(xpt - mcYpt);
      left.setYpt(ypt);
      right.setYpt(ypt);
    }

    /** Method to set mc value.
     *
     * @param mcYpoint mc value for ypoint
     */
    public void setMc(final float mcYpoint) {
      this.mcYpt = mcYpoint;
      right.setXpt(xpt + mcYpt);
      left.setXpt(xpt - mcYpt);
    }

    /** Method to set x coordinate.
     *
     * @param xpoint x coordinate
     */
    public void setX(final float xpoint) {
      this.xpt = xpoint;
      right.setXpt(xpt + mcYpt);
      left.setXpt(xpt - mcYpt);
    }

    /** Method to set y coordinate.
     *
     * @param ypoint y coordinate
     */
    public void setY(final float ypoint) {
      this.ypt = ypoint;
      left.setYpt(ypt);
      right.setYpt(ypt);
    }

    public void setLeftX(final float leftX) {
      left.setXpt(leftX);
      xpt = (left.getXpt() + right.getXpt()) / 2;
    }

    @Override
    public String toString() {
      return "YPoint{" + "x=" + xpt + ", y=" + ypt + ", mc=" + mcYpt
              + ", left=" + left + ", right=" + right + '}';
    }
  }

}
