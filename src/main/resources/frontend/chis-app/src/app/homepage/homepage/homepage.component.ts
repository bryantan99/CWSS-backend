import {Component, OnInit, ViewChild} from '@angular/core';
import {PostFeedComponent} from "../../shared/components/post-feed/post-feed.component";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html'
})
export class HomepageComponent implements OnInit {

  @ViewChild(PostFeedComponent) postFeedComponent: PostFeedComponent;

  nzSelectedIndex = 0;
  newPostModalIsVisible: boolean = false;

  constructor() {
  }

  ngOnInit(): void {
  }

  openNewPostModal() {
    this.newPostModalIsVisible = true;
  }

  modalVisibleHasChange(isVisible: boolean) {
    this.newPostModalIsVisible = isVisible;
  }

  postListHasChanges(hasNewPost: boolean) {
    if (hasNewPost) {
      this.postFeedComponent.getAdminPosts();
    }
  }
}
