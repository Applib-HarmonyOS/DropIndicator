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
package comulez.github.dropindicatorsample.slice;

import comulez.github.dropindicator.DropIndicator;
import comulez.github.dropindicator.ResourceTable;
import comulez.github.dropindicatorsample.CustomPagerAdapter;
import java.util.ArrayList;
import java.util.List;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.PageSlider;

public class MainAbilitySlice extends AbilitySlice {

  @Override
  public void onStart(final Intent intent) {
    super.onStart(intent);
    setUIContent(ResourceTable.Layout_ability_main);
    initDrops();
  }

  private void initDrops() {
    final PageSlider pageSlider = (PageSlider) findComponentById(ResourceTable.Id_viewpager);
    pageSlider.setPageMargin(20);
    pageSlider.setProvider(new CustomPagerAdapter(this, getData()));
    final DropIndicator dropIndicator =
        (DropIndicator) findComponentById(ResourceTable.Id_circleIndicator);
    dropIndicator.setViewPager(pageSlider);
  }


  private List<CustomPagerAdapter.DataItem> getData() {
    final List<CustomPagerAdapter.DataItem> dataItems = new ArrayList<>();
    dataItems.add(new CustomPagerAdapter.DataItem(ResourceTable.Media_pos0));
    dataItems.add(new CustomPagerAdapter.DataItem(ResourceTable.Media_pos1));
    dataItems.add(new CustomPagerAdapter.DataItem(ResourceTable.Media_pos2));
    dataItems.add(new CustomPagerAdapter.DataItem(ResourceTable.Media_pos3));
    return dataItems;
  }

  @Override
  public void onActive() {
    super.onActive();
  }

  @Override
  public void onForeground(final Intent intent) {
    super.onForeground(intent);
  }
}
