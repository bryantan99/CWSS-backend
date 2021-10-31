import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SharedModule} from "../shared/shared.module";
import { SettingsComponent } from './settings.component';
import {UserModule} from "../user/user.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { AccountSettingComponent } from './account-setting/account-setting.component';
import {RouterModule} from "@angular/router";



@NgModule({
  declarations: [
    SettingsComponent,
    AccountSettingComponent
  ],
    imports: [
        CommonModule,
        SharedModule,
        UserModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule
    ]
})
export class SettingsModule { }