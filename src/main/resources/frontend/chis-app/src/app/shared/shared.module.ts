import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NewsfeedComponent} from './components/newsfeed/newsfeed.component';
import {PostFeedComponent} from "./components/post-feed/post-feed.component";
import {SHARED_ZORRO_MODULES} from "./shared-zorro.module";


@NgModule({
  declarations: [NewsfeedComponent, PostFeedComponent],
  imports: [
    CommonModule,
    ...SHARED_ZORRO_MODULES
  ],
  exports: [
    CommonModule,
    NewsfeedComponent,
    PostFeedComponent,
    ...SHARED_ZORRO_MODULES
  ]
})
export class SharedModule {
}
