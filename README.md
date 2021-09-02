DropIndicator
=================
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_DropIndicator&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_DropIndicator)
[![.github/workflows/main.yml](https://github.com/applibgroup/DropIndicator/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/DropIndicator/actions/workflows/main.yml)

Introduction
------------
HMOS library which handles custom animation on indicator of PageSlider 

Source
------------

The code in this repository was inspired from [Ulez/DropIndicator](https://github.com/Ulez/DropIndicator). We are very
 thankful to Ulez.

Screenshot
------------

![screenshot](/screenshots/drop1.PNG)
![screenshot](/screenshots/drop2.PNG)
![screenshot](/screenshots/drop3.PNG)
![screenshot](/screenshots/drop4.PNG)

Installation
------------

In order to use the library, add the following line to your **root** gradle file:

I) For using DropIndicator module in sample app, include the source code and add the below
 dependencies in entry/build.gradle to generate hap/support.har.
```
dependencies {
        implementation project(path: ':dropindicator')
        implementation fileTree(dir: 'libs', include: ['*.har'])
        testImplementation 'junit:junit:4.13'
}
```
II) For using DropIndicator in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```
dependencies {
        implementation fileTree(dir: 'libs', include: ['*.har'])
        testImplementation 'junit:junit:4.12'
}
```
III) For using DropIndicator from a remote repository in separate application, add the
 below dependencies in entry/build.gradle file.
```
	dependencies {
           implementation 'dev.applibgroup:dropindicator:1.0.0'
           testCompile 'junit:junit:4.12'
	}
```

Usage
-----

I). Declare Custom indicator & Page slider in XML (see xml attributes below for customization):

	<comulez.github.droplibrary.DropViewPager
            ohos:id="$+id:viewpager"
            ohos:width="match_parent"
            ohos:height="200vp"
            ohos:align_parent_top="true"
            ohos:margin="15vp"
            ohos:above="$id:circleIndicator"/>
            
	<comulez.github.droplibrary.DropIndicator
            ohos:id="$+id:circleIndicator"
            ohos:width="match_parent"
            ohos:height="50vp"
            ohos:bottom_margin="10vp"
            ohos:align_parent_bottom="true"
            app:circle_color="#ffaaaaaa"
            app:click_color="#ffffff"
            app:color1="#FCC04D"
            app:color2="#00C3E2"
            app:color3="#FE626D"
            app:color4="#966ACF"
            app:duration="900"
            app:radius="16vp"
            app:scale="0.7">
            <Image
                ohos:height="match_content"
                ohos:width="match_content"
                ohos:scale_mode="stretch"
                ohos:image_src="$media:msg">
            </Image>
            .....
    </comulez.github.droplibrary.DropIndicator>

II). Usage in java 
            
	PageSlider pageSlider = (PageSlider) findComponentById(ResourceTable.Id_viewpager);
        pageSlider.setPageMargin(20);
        pageSlider.setProvider(new CustomPagerAdapter(this, getData())); // Create a custom
         adaptor and pass the images to display in the page slider
    DropIndicator dropIndicator = (DropIndicator) findComponentById(ResourceTable
    .Id_circleIndicator);
        dropIndicator.setViewPager(pageSlider);
License
-------
	Copyright 2016 Ulez 
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
