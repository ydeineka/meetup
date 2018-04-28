import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpResponse, HttpEventType} from "@angular/common/http";
import {AccountService} from "../account.service";
import {Profile} from "../profile";
import {ActivatedRoute, Router} from "@angular/router"
import {UploadFileService} from "../../upload.file/upload.file.service";
import {NgxSpinnerService} from "ngx-spinner";
import {Popup} from "ng2-opd-popup";


@Component({
  selector: 'edit',
  styleUrls: ['./edit.component.css'],
  templateUrl: './edit.component.html'
})

export class EditComponent implements OnInit {

  success: boolean = false;
  error: string;
  selectedFiles: FileList;
  currentFileUpload: File;
  progress: { percentage: number } = { percentage: 0 };
  account: Profile;
  profile: Profile;
  state: string = "profile";
  errorFileFormat: string;
  private showModal: boolean;


  constructor(private accountService: AccountService,
              private router: Router,
              private route: ActivatedRoute,
              private uploadService: UploadFileService,
              private spinner: NgxSpinnerService,
              private popup: Popup) {

    this.account = new Profile();
  }

  clickButton(){
    this.popup.show();
  }
  close(){
    this.popup.hide();
  }

  ngOnInit() {
    this.profile = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.account.id = params['id'];
    });

    this.accountService.profile(this.account).subscribe(
      (data) => {
        this.account = data;
      }
    );

    console.log(this.account);
  }

  save(){
    this.spinner.show();

    this.accountService.update(this.account).subscribe(
      () => {
        this.success = true;
        this.spinner.hide();
        this.router.navigate(
          [JSON.parse(localStorage.currentUser).id + '/profile']);
      },
      response => {
        this.spinner.hide();
        this.processError(response)
      }
    );
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
    var filename:string = this.selectedFiles.item(0).name.toLowerCase();
    if(!filename.endsWith(".jpg")&&
      !filename.endsWith(".png")&&
      !filename.endsWith(".gif")){
      this.errorFileFormat = "Bad fromat for file "+this.selectedFiles.item(0).name;
    } else{
      this.errorFileFormat = null;
    }
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
      }
    })

    this.selectedFiles = undefined
  }

  formatDate(date: Date) {
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    this.account.birthDay = `${day}-${month}-${year}`;
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    this.error = 'ERROR';
  }

  changeWishList() {

  }

}
