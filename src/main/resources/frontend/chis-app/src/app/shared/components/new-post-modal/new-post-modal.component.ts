import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AdminPostService} from "../../services/admin-post.service";

@Component({
  selector: 'app-new-post-modal',
  templateUrl: './new-post-modal.component.html'
})
export class NewPostModalComponent implements OnInit {
  @Input() isVisible: boolean;
  @Output() modalIsVisibleEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() addNewPostEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();

  nzTitle: string = "Create New Post";
  postForm: FormGroup;
  isSubmitted: boolean = false;

  constructor(private fb: FormBuilder,
              private adminPostService: AdminPostService) { }

  ngOnInit(): void {
    this.postForm = this.fb.group({
      postDescription: ['', Validators.required]
    });
  }

  handleOk(): void {
    if (this.postForm.valid) {
      this.isSubmitted = true;

      this.adminPostService.addAdminPost(this.postForm.value)
        .subscribe(resp => {
          if (resp) {
            this.isVisible = false;
            this.emitIsVisible();
            this.emitAddNewPost();
            this.resetForm();
            this.isSubmitted = false;
          }
        }, error => {
          console.log("There's an error when creating new post.", error);
          this.isSubmitted = false;
        });
    }
  }

  handleCancel() {
    this.isVisible = false;
    this.resetForm();
    this.emitIsVisible();
  }

  emitIsVisible() {
    this.modalIsVisibleEmitter.emit(this.isVisible);
  }

  private resetForm() {
    this.postForm.reset();
  }

  private emitAddNewPost() {
    this.addNewPostEmitter.emit(true);
  }
}
