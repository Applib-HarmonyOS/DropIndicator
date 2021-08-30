package comulez.github.dropindicator;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DragEvent;
import ohos.agp.components.PageSlider;
import ohos.app.Context;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

/** Created by Ulez on 2017/2/19.
 * Email：lcy1532110757@gmail.com.
 */
public class DropViewPager extends PageSlider implements Component.TouchEventListener, Touchable {
  private boolean touchable = true;

  public static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "LIBTEST");

  @Override
  public void setTouchable(final boolean touchable) {
    this.touchable = touchable;
  }

  public DropViewPager(final Context context) {
    super(context);
  }

  public DropViewPager(final Context context, final AttrSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onDrag(final Component component, final DragEvent event) {
    boolean res = false;
    if (touchable) {
      res =  super.onDrag(component, event);
    }
    return res;
  }

  @Override
  public boolean onTouchEvent(final Component component, final TouchEvent touchEvent) {
    return touchable;
  }
}
