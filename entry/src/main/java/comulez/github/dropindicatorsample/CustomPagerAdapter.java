/*
 * Copyright(C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on as "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or impiles.
 * See the License for the specific language governing permissions and
 * Limitations under the License.
 */
package comulez.github.dropindicatorsample;

import java.util.List;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.Context;


public class CustomPagerAdapter extends PageSliderProvider {
  private Context mcontext;
  private List<DataItem> pagerList;

  public CustomPagerAdapter(final Context context, final List<DataItem> pagerList) {
    this.mcontext = context;
    this.pagerList = pagerList;
  }

  @Override
  public int getCount() {
    return pagerList.size();
  }

  @Override
  public Object createPageInContainer(final ComponentContainer compContainer, final int pos) {
    final DataItem data = pagerList.get(pos);
    final Image image = new Image(mcontext);
    final ShapeElement element = new ShapeElement();
    element.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#21293A")));
    image.setBackground(element);
    image.setPixelMap(data.res);
    image.setScaleMode(Image.ScaleMode.ZOOM_CENTER);
    image.setLayoutConfig(
            new DependentLayout.LayoutConfig(
                    ComponentContainer.LayoutConfig.MATCH_PARENT,
                    ComponentContainer.LayoutConfig.MATCH_PARENT
            ));

    compContainer.addComponent(image);
    return image;
  }

  @Override
  public void destroyPageFromContainer(final ComponentContainer compContainer, final int pos,
                                       final Object object) {
    compContainer.removeComponent((Component) object);
  }

  @Override
  public boolean isPageMatchToObject(final Component component, final Object object) {
    return true;
  }

  public static class DataItem {
    private int res;

    public DataItem(final int mres) {
      res = mres;
    }
  }
}
