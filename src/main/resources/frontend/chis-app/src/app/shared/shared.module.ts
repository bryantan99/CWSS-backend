import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NzTabsModule} from "ng-zorro-antd/tabs";
import { NewsfeedComponent } from './components/newsfeed/newsfeed.component';
import {PostFeedComponent} from "./components/post-feed/post-feed.component";
import {NzCardModule} from "ng-zorro-antd/card";
import {NzGridModule} from "ng-zorro-antd/grid";
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzDividerModule} from "ng-zorro-antd/divider";
import {NzTypographyModule} from "ng-zorro-antd/typography";
import {NzSpinModule} from "ng-zorro-antd/spin";



@NgModule({
  declarations: [NewsfeedComponent, PostFeedComponent],
  imports: [
    CommonModule,
    NzCardModule,
    NzGridModule,
    NzButtonModule,
    NzDividerModule,
    NzTypographyModule,
    NzSpinModule
  ],
  exports: [
    CommonModule,
    NzTabsModule,
    NewsfeedComponent,
    PostFeedComponent
  ]
})
export class SharedModule { }
