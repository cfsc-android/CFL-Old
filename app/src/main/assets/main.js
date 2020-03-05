webpackJsonp([0],{

/***/ 102:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MyPostPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__post_details_post_details__ = __webpack_require__(50);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__slides_slides__ = __webpack_require__(40);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};




var MyPostPage = MyPostPage_1 = (function () {
    function MyPostPage(navCtrl, navParams, loadingCtrl, service) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.loadingCtrl = loadingCtrl;
        this.service = service;
        // 获取 baseUrl
        this.baseUrl = JSON.parse(window.localStorage.getItem('urls')).baseUrl;
        // 获取我的信息
        this.myInfo = window['myInfo'];
        // 定义页码
        this.pageNum = 1;
        // 定义获取数据状态
        this.noData = false;
        // 定义幻灯片数组
        this.slidersImageList = [];
    }
    MyPostPage.prototype.ionViewDidLoad = function () {
        console.log('我是: ' + this.myInfo.nickname + ', 我进入了自己的帖子中心。');
        this.getMyPostList();
    };
    // 获取我的帖子列表
    MyPostPage.prototype.getMyPostList = function () {
        var _this = this;
        // 定义遮罩对象
        var loader = this.loadingCtrl.create({
            content: "请稍等...",
            cssClass: 'haha'
        });
        loader.present();
        this.pageNum = 1;
        this.service.getMyPostList(this.myInfo.id, this.pageNum)
            .then(function (resRes) {
            if (resRes.resultCode === '0') {
                _this.postList = resRes.data;
                console.log(resRes.data);
                _this.pageNum++;
            }
            else {
                setTimeout(function () {
                    alert('暂未给游客身份设置发帖权限');
                    _this.navCtrl.pop();
                }, 1200);
            }
            loader.dismiss();
        });
    };
    MyPostPage.prototype.ionViewWillEnter = function () {
        // 发帖成功时，刷新
        /* if (window.localStorage.getItem('isAddPost') === 'yes') {
          this.getMyPostList();
          window.localStorage.setItem('isAddPost', 'no');
        } */
        // 同步点赞数据
        if (this.postList && this.postList.length > 0 && window.localStorage.getItem('recordThumb') != '[]') {
            console.log('indexof...');
            var arr = JSON.parse(window.localStorage.getItem('recordThumb'));
            for (var i = 0; i < arr.length; i++) {
                for (var j = 0; j < this.postList.length; j++) {
                    var item = this.postList[j];
                    if (item.id == arr[i].id) {
                        item.upnumber = arr[i].count;
                        break;
                    }
                }
            }
        }
    };
    // 发帖（原生）
    MyPostPage.prototype.editPost = function () {
        // 在 window 对象上绑定 getMyPostList 方法 与 上下文
        window['getMyPostListContext'] = this;
        window['getMyPostList'] = this.getMyPostList;
        // 去往原生端发帖页面
        window['javaInterface'] && window['javaInterface'].editPost && window['javaInterface'].editPost();
        window['toYun'] && window['toYun'].editPost && window['toYun'].editPost();
    };
    // 返回
    MyPostPage.prototype.back = function () {
        this.navCtrl.pop(MyPostPage_1);
    };
    // 打开帖子详情
    MyPostPage.prototype.openPostDetails = function (postData) {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_2__post_details_post_details__["a" /* PostDetailsPage */], {
            postId: postData.id
        });
    };
    // 删除帖子数据
    MyPostPage.prototype.deleteDataFun = function (postCard) {
        // 初始高度
        var start = postCard.clientHeight;
        // 获取 styleInfo（兼容）
        var styleInfo = postCard.currentStyle ? postCard.currentStyle : document.defaultView.getComputedStyle(postCard, null);
        // 初始Padding
        var padding = parseFloat(styleInfo.paddingTop);
        // 当前高度初始高度
        var current = start;
        // 设置 步长
        var step;
        var percent = 1;
        var timer = setInterval(function () {
            if (padding > 0) {
                padding -= 5;
                postCard.style.paddingTop = padding + 'px';
            }
            step = current / 10;
            current -= step;
            percent = current / start;
            if (current < 3) {
                current = 0;
                percent = 0;
                // postCard.style.display = 'none';
                // postCard.style.display = 'none';
                clearInterval(timer);
            }
            postCard.style.height = current + 'px';
            postCard.style.opacity = percent;
        }, 24);
    };
    // 上拉加载
    MyPostPage.prototype.doInfinite = function (infiniteScroll) {
        var _this = this;
        console.log('Begin async operation');
        this.service.getMyPostList(this.myInfo.id, this.pageNum)
            .then(function (resRes) {
            if (resRes.data.length === 0) {
                infiniteScroll.enable(false);
                _this.noData = true;
                return;
            }
            _this.pageNum++;
            _this.postList = _this.postList.concat(resRes.data);
            console.log(_this.postList);
            console.log('Async operation has ended');
            infiniteScroll.complete();
        });
    };
    // 下拉刷新
    MyPostPage.prototype.doRefresh = function (refresher) {
        console.log('Begin async operation', refresher);
        this.getMyPostList();
        console.log('Async operation has ended');
        refresher.complete();
    };
    // 打开幻灯片
    MyPostPage.prototype.goSlides = function (imageList, index) {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_3__slides_slides__["a" /* SlidesPage */], {
            imageList: imageList,
            index: index
        });
    };
    return MyPostPage;
}());
MyPostPage = MyPostPage_1 = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'page-my-post',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\pages\my-post\my-post.html"*/'<ion-header>\n  <ion-item color="primary" no-lines>\n\n    <button class="nav-btn" ion-button clear color="light" icon-only (click)="back()">\n      <ion-icon name="ios-arrow-back"></ion-icon>\n    </button>\n\n    <ion-title text-center>\n      我的帖子\n    </ion-title>\n\n    <button item-right class="nav-btn" ion-button clear color="light" small (click)="editPost()">\n      发帖\n    </button>\n\n  </ion-item>\n</ion-header>\n\n<ion-content>\n\n  <ion-card *ngFor="let p of postList" #postCardRef>\n\n    <div class="wrap">\n      <ion-item>\n        <ion-avatar item-start item-left>\n          <img [src]="p.avatar ? (baseUrl + p.avatar) : \'./assets/images/default-avatar.png\'">\n        </ion-avatar>\n        <h2 item-left>\n          {{p.publishName}}\n        </h2>\n        <p item-right>\n          {{p.publishTime}}\n        </p>\n      </ion-item>\n\n      <ion-card-content>\n        <p class="ellipsis" (click)="openPostDetails(p)">\n          {{p.body}}\n        </p>\n        <ion-grid *ngIf="p.imageList">\n          <ion-row>\n            <ion-col col-4 *ngFor="let img of p.imageList; let i = index">\n              <div #imgBoxRef (click)="goSlides(p.imageList, i)" class="post-img" [ngStyle]="{\'background-image\': \'url(\'+baseUrl+img.url+\')\', \'height\': imgBoxRef.clientWidth + \'px\'}"></div>\n            </ion-col>\n          </ion-row>\n        </ion-grid>\n      </ion-card-content>\n\n      <div class="footer">\n        <div float-right>\n          <thumb [postId]="p.id" [thumbCount]="p.upnumber"></thumb>\n          <delete-post *ngIf="p.publishId == myInfo.id" [postId]="p.id" (deleteData)="deleteDataFun(postCardRef)"></delete-post>\n        </div>\n      </div>\n    </div>\n\n  </ion-card>\n\n  <ion-refresher (ionRefresh)="doRefresh($event)">\n    <ion-refresher-content\n      pullingIcon="arrow-round-down"\n      pullingText="下拉刷新"\n      refreshingSpinner="circles"\n      refreshingText="加载中...">\n    </ion-refresher-content>\n  </ion-refresher>\n\n <ion-infinite-scroll (ionInfinite)="doInfinite($event)">\n   <ion-infinite-scroll-content loadingText="加载更多..."></ion-infinite-scroll-content>\n </ion-infinite-scroll>\n\n  <div class="noData" *ngIf="noData === true" text-center>没有更多数据了哦 ^_^</div>\n\n</ion-content>'/*ion-inline-end:"E:\Development\neighbors\Code\src\pages\my-post\my-post.html"*/,
    }),
    __param(3, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('postService')),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* LoadingController */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* LoadingController */]) === "function" && _c || Object, Object])
], MyPostPage);

var MyPostPage_1, _a, _b, _c;
//# sourceMappingURL=my-post.js.map

/***/ }),

/***/ 103:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return VisitPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__post_details_post_details__ = __webpack_require__(50);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__slides_slides__ = __webpack_require__(40);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};




var VisitPage = VisitPage_1 = (function () {
    function VisitPage(navCtrl, navParams, loadingCtrl, service, urlsData) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.loadingCtrl = loadingCtrl;
        this.service = service;
        this.urlsData = urlsData;
        // 获取 baseUrl
        this.baseUrl = JSON.parse(window.localStorage.getItem('urls')).baseUrl;
        // 获取我的信息
        this.myInfo = window['myInfo'];
        // 定义页码
        this.pageNum = 1;
        // 定义获取数据状态
        this.noData = false;
        // 定义幻灯片数组
        this.slidersImageList = [];
        // 获取主人信息
        this.hostInfo = this.navParams.data.hostInfo;
    }
    VisitPage.prototype.ionViewDidLoad = function () {
        console.log('我是: ' + this.myInfo.nickname + ', 我正在拜访' + this.hostInfo.nickname + '。');
        this.getHostPostList();
    };
    // 获取帖子列表
    VisitPage.prototype.getHostPostList = function () {
        var _this = this;
        // 定义遮罩对象
        var loader = this.loadingCtrl.create({
            content: "请稍等..."
        });
        loader.present();
        this.pageNum = 1;
        this.service.getMyPostList(this.hostInfo.id, this.pageNum)
            .then(function (resRes) {
            _this.postList = resRes.data;
            console.log(resRes.data);
            _this.pageNum++;
            loader.dismiss();
        });
    };
    VisitPage.prototype.ionViewWillEnter = function () {
        // 同步点赞数据
        if (this.postList && this.postList.length > 0 && window.localStorage.getItem('recordThumb') != '[]') {
            console.log('indexof...');
            var arr = JSON.parse(window.localStorage.getItem('recordThumb'));
            for (var i = 0; i < arr.length; i++) {
                for (var j = 0; j < this.postList.length; j++) {
                    var item = this.postList[j];
                    if (item.id == arr[i].id) {
                        item.upnumber = arr[i].count;
                        break;
                    }
                }
            }
        }
    };
    // 返回
    VisitPage.prototype.back = function () {
        this.navCtrl.pop(VisitPage_1);
    };
    // 打开帖子详情
    VisitPage.prototype.openPostDetails = function (postData) {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_2__post_details_post_details__["a" /* PostDetailsPage */], {
            postId: postData.id
        });
    };
    // 上拉加载
    VisitPage.prototype.doInfinite = function (infiniteScroll) {
        var _this = this;
        console.log('Begin async operation');
        this.service.getMyPostList(this.hostInfo.id, this.pageNum)
            .then(function (resRes) {
            if (resRes.data.length === 0) {
                infiniteScroll.enable(false);
                _this.noData = true;
                return;
            }
            _this.pageNum++;
            _this.postList = _this.postList.concat(resRes.data);
            console.log(_this.postList);
            console.log('Async operation has ended');
            infiniteScroll.complete();
        });
    };
    // 下拉刷新
    VisitPage.prototype.doRefresh = function (refresher) {
        console.log('Begin async operation', refresher);
        this.getHostPostList();
        console.log('Async operation has ended');
        refresher.complete();
    };
    // 打开幻灯片
    VisitPage.prototype.goSlides = function (imageList, index) {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_3__slides_slides__["a" /* SlidesPage */], {
            imageList: imageList,
            index: index
        });
    };
    return VisitPage;
}());
VisitPage = VisitPage_1 = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'page-visit',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\pages\visit\visit.html"*/'<ion-header>\n  <ion-item color="primary" no-lines>\n\n    <button class="nav-btn" ion-button clear color="light" icon-only (click)="back()">\n      <ion-icon name="ios-arrow-back"></ion-icon>\n    </button>\n\n    <ion-title text-center>\n      {{hostInfo.nickname}}\n    </ion-title>\n\n  </ion-item>\n</ion-header>\n\n\n<ion-content>\n\n  <ion-card *ngFor="let p of postList">\n    <div class="wrap">\n      <ion-item>\n        <ion-avatar item-start item-left>\n          <img [src]="p.avatar ? (baseUrl + p.avatar) : \'./assets/images/default-avatar.png\'">\n        </ion-avatar>\n        <h2 item-left>\n          {{p.publishName}}\n        </h2>\n        <p item-right>\n          {{p.publishTime}}\n        </p>\n      </ion-item>\n\n      <ion-card-content>\n        <p class="ellipsis" (click)="openPostDetails(p)">\n          {{p.body}}\n        </p>\n        <ion-grid>\n          <ion-row>\n            <ion-col col-4 *ngFor="let img of p.imageList; let i = index;">\n              <div #imgBoxRef (click)="goSlides(p.imageList, i)" class="post-img" [ngStyle]="{\'background-image\': \'url(\'+baseUrl+img.url+\')\', \'height\': imgBoxRef.clientWidth + \'px\'}"></div>\n            </ion-col>\n          </ion-row>\n        </ion-grid>\n      </ion-card-content>\n\n      <thumb float-right [postId]="p.id" [thumbCount]="p.upnumber"></thumb>\n    </div>\n  </ion-card>\n\n\n  <ion-refresher (ionRefresh)="doRefresh($event)">\n    <ion-refresher-content pullingIcon="arrow-round-down" pullingText="下拉刷新" refreshingSpinner="circles" refreshingText="加载中...">\n    </ion-refresher-content>\n  </ion-refresher>\n\n  <ion-infinite-scroll (ionInfinite)="doInfinite($event)">\n    <ion-infinite-scroll-content loadingText="加载更多..."></ion-infinite-scroll-content>\n  </ion-infinite-scroll>\n\n  <div class="noData" *ngIf="noData === true" text-center>没有更多数据了哦 ^_^</div>\n\n</ion-content>'/*ion-inline-end:"E:\Development\neighbors\Code\src\pages\visit\visit.html"*/,
    }),
    __param(3, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('postService')),
    __param(4, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('urls')),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* LoadingController */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* LoadingController */]) === "function" && _c || Object, Object, Object])
], VisitPage);

var VisitPage_1, _a, _b, _c;
//# sourceMappingURL=visit.js.map

/***/ }),

/***/ 112:
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	return new Promise(function(resolve, reject) { reject(new Error("Cannot find module '" + req + "'.")); });
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = 112;

/***/ }),

/***/ 153:
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	return new Promise(function(resolve, reject) { reject(new Error("Cannot find module '" + req + "'.")); });
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = 153;

/***/ }),

/***/ 201:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HomePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__my_post_my_post__ = __webpack_require__(102);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__visit_visit__ = __webpack_require__(103);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__post_details_post_details__ = __webpack_require__(50);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__slides_slides__ = __webpack_require__(40);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};






var HomePage = (function () {
    function HomePage(navCtrl, loadingCtrl, service) {
        this.navCtrl = navCtrl;
        this.loadingCtrl = loadingCtrl;
        this.service = service;
        // 获取 baseUrl
        this.baseUrl = JSON.parse(window.localStorage.getItem('urls')).baseUrl;
        // 获取我的信息
        this.myInfo = window['myInfo'];
        // 定义页码
        this.pageNum = 1;
        // 定义获取数据状态
        this.noData = false;
        // 定义幻灯片数组
        this.slidersImageList = [];
        this.isRefresh = true;
    }
    HomePage.prototype.ionViewDidLoad = function () {
        console.log('我是: ' + this.myInfo.nickname + ', 我在邻里首页。');
        this.getPostList();
    };
    // 获取帖子列表
    HomePage.prototype.getPostList = function () {
        var _this = this;
        // alert('我是: ' + this.myInfo.nickname + ', 我在邻里首页。');
        // 定义遮罩对象
        var loader = this.loadingCtrl.create({
            content: "请稍等..."
        });
        loader.present();
        this.pageNum = 1;
        this.service.getPostList(this.pageNum)
            .then(function (res) {
            _this.postList = res.data;
            console.log(_this.postList);
            _this.pageNum++;
            loader.dismiss();
        });
    };
    HomePage.prototype.ionViewWillEnter = function () {
        if (window.localStorage.getItem('recordThumb') == null) {
            window.localStorage.setItem('recordThumb', '[]');
        }
        else if (window.localStorage.getItem('recordThumb') != '[]') {
            console.log('indexof...');
            var arr = JSON.parse(window.localStorage.getItem('recordThumb'));
            for (var i = 0; i < arr.length; i++) {
                for (var j = 0; j < this.postList.length; j++) {
                    var item = this.postList[j];
                    if (item.id == arr[i].id) {
                        item.upnumber = arr[i].count;
                        break;
                    }
                }
            }
            window.localStorage.setItem('recordThumb', '[]');
        }
    };
    // 我的帖子
    HomePage.prototype.myPost = function () {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_2__my_post_my_post__["a" /* MyPostPage */]);
    };
    // 拜访邻居
    HomePage.prototype.visitNeighbor = function (publishName, publishId) {
        // 自己拜访自己
        if (publishId == this.myInfo.id) {
            this.myPost();
            return;
        }
        // 拜访邻居
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_3__visit_visit__["a" /* VisitPage */], {
            hostInfo: {
                nickname: publishName,
                id: publishId
            }
        });
    };
    // 打开帖子详情
    HomePage.prototype.openPostDetails = function (postId) {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_4__post_details_post_details__["a" /* PostDetailsPage */], {
            postId: postId
        });
    };
    // 删除帖子数据
    HomePage.prototype.deleteDataFun = function (postCard) {
        // 初始高度
        var start = postCard.clientHeight;
        // 获取 styleInfo（兼容）
        var styleInfo = postCard.currentStyle ? postCard.currentStyle : document.defaultView.getComputedStyle(postCard, null);
        // 初始Padding
        var padding = parseFloat(styleInfo.paddingTop);
        // 当前高度初始高度
        var current = start;
        // 设置 步长
        var step;
        var percent = 1;
        var timer = setInterval(function () {
            if (padding > 0) {
                padding -= 5;
                postCard.style.paddingTop = padding + 'px';
            }
            step = current / 10;
            current -= step;
            percent = current / start;
            if (current < 3) {
                current = 0;
                percent = 0;
                // postCard.style.display = 'none';
                // postCard.style.display = 'none';
                clearInterval(timer);
            }
            postCard.style.height = current + 'px';
            postCard.style.opacity = percent;
        }, 24);
    };
    // 上拉加载
    HomePage.prototype.doInfinite = function (infiniteScroll) {
        var _this = this;
        console.log('Begin async operation');
        this.service.getPostList(this.pageNum)
            .then(function (res) {
            if (res.data.length === 0) {
                infiniteScroll.enable(false);
                _this.noData = true;
                return;
            }
            _this.pageNum++;
            _this.postList = _this.postList.concat(res.data);
            console.log(_this.postList);
            console.log('Async operation has ended');
            infiniteScroll.complete();
        });
    };
    // 下拉刷新
    HomePage.prototype.doRefresh = function (refresher) {
        console.log('Begin async operation', refresher);
        this.getPostList();
        console.log('Async operation has ended');
        refresher.complete();
    };
    // 打开幻灯片
    HomePage.prototype.goSlides = function (imageList, index) {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_5__slides_slides__["a" /* SlidesPage */], {
            imageList: imageList,
            index: index
        });
        this.isRefresh = false;
    };
    // 返回原生端
    HomePage.prototype.backToApp = function () {
        window['javaInterface'] && window['javaInterface'].backToApp && window['javaInterface'].backToApp();
        window['toYun'] && window['toYun'].backToApp && window['toYun'].backToApp();
    };
    return HomePage;
}());
HomePage = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'page-home',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\pages\home\home.html"*/'<ion-header>\n  <ion-item color="primary" no-lines>\n\n    <button class="nav-btn" ion-button clear color="light" icon-only (click)="backToApp()">\n      <ion-icon name="ios-arrow-back"></ion-icon>\n    </button>\n\n    <ion-title text-center>邻里</ion-title>\n\n    <button item-right class="nav-btn" ion-button clear color="light" small (click)="myPost()">\n      我的帖子\n    </button>\n\n\n\n  </ion-item>\n</ion-header>\n\n<ion-content>\n\n  <ion-card *ngFor="let p of postList" #postCardRef>\n    <div class="wrap">\n\n      <ion-item (click)="visitNeighbor(p.publishName, p.publishId)">\n        <ion-avatar item-start item-left>\n          <img [src]="p.avatar ? (baseUrl + p.avatar) : \'./assets/images/default-avatar.png\'">\n        </ion-avatar>\n        <h2 item-left>\n          {{p.publishName}}\n        </h2>\n        <p item-right>\n          {{p.publishTime}}\n        </p>\n      </ion-item>\n\n      <ion-card-content>\n        <p (click)="openPostDetails(p.id)" class="ellipsis">\n          {{p.body}}\n        </p>\n        <ion-grid *ngIf="p.imageList">\n          <ion-row>\n            <ion-col col-4 *ngFor="let img of p.imageList; let i = index">\n              <div #imgBoxRef (click)="goSlides(p.imageList, i)" class="post-img" [ngStyle]="{\'background-image\': \'url(\'+baseUrl+img.url+\')\', \'height\': imgBoxRef.clientWidth + \'px\'}"></div>\n            </ion-col>\n          </ion-row>\n        </ion-grid>\n      </ion-card-content>\n\n      <div class="footer">\n        <div float-right>\n          <thumb [postId]="p.id" [thumbCount]="p.upnumber"></thumb>\n          <delete-post *ngIf="p.publishId == myInfo.id" [postId]="p.id" (deleteData)="deleteDataFun(postCardRef)"></delete-post>\n        </div>\n      </div>\n\n    </div>\n  </ion-card>\n\n  <ion-refresher (ionRefresh)="doRefresh($event)">\n    <ion-refresher-content\n      pullingIcon="arrow-round-down"\n      pullingText="下拉刷新"\n      refreshingSpinner="circles"\n      refreshingText="加载中...">\n    </ion-refresher-content>\n  </ion-refresher>\n\n <ion-infinite-scroll (ionInfinite)="doInfinite($event)">\n   <ion-infinite-scroll-content loadingText="加载更多..."></ion-infinite-scroll-content>\n </ion-infinite-scroll>\n\n  <div class="noData" *ngIf="noData === true" text-center>没有更多数据了哦 ^_^</div>\n\n</ion-content>\n'/*ion-inline-end:"E:\Development\neighbors\Code\src\pages\home\home.html"*/
    }),
    __param(2, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('postService')),
    __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */],
        __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* LoadingController */], Object])
], HomePage);

//# sourceMappingURL=home.js.map

/***/ }),

/***/ 202:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser_dynamic__ = __webpack_require__(203);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__app_module__ = __webpack_require__(221);


Object(__WEBPACK_IMPORTED_MODULE_0__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_1__app_module__["a" /* AppModule */]);
//# sourceMappingURL=main.js.map

/***/ }),

/***/ 221:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__(26);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__ = __webpack_require__(193);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__ionic_native_status_bar__ = __webpack_require__(196);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__angular_http__ = __webpack_require__(197);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__angular_common__ = __webpack_require__(30);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7_ng2_file_upload__ = __webpack_require__(270);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7_ng2_file_upload___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_7_ng2_file_upload__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__providers_post_service_post_service__ = __webpack_require__(273);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__app_component__ = __webpack_require__(276);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__pages_home_home__ = __webpack_require__(201);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__pages_my_post_my_post__ = __webpack_require__(102);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__pages_edit_post_edit_post__ = __webpack_require__(277);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_13__pages_post_details_post_details__ = __webpack_require__(50);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_14__pages_visit_visit__ = __webpack_require__(103);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_15__pages_slides_slides__ = __webpack_require__(40);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_16__components_thumb_thumb__ = __webpack_require__(278);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_17__providers_urls_urls__ = __webpack_require__(279);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_18__components_delete_post_delete_post__ = __webpack_require__(280);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



















var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_1__angular_core__["NgModule"])({
        declarations: [
            __WEBPACK_IMPORTED_MODULE_9__app_component__["a" /* MyApp */],
            __WEBPACK_IMPORTED_MODULE_16__components_thumb_thumb__["a" /* ThumbComponent */],
            __WEBPACK_IMPORTED_MODULE_18__components_delete_post_delete_post__["a" /* DeletePostComponent */],
            __WEBPACK_IMPORTED_MODULE_10__pages_home_home__["a" /* HomePage */],
            __WEBPACK_IMPORTED_MODULE_11__pages_my_post_my_post__["a" /* MyPostPage */],
            __WEBPACK_IMPORTED_MODULE_12__pages_edit_post_edit_post__["a" /* EditPostPage */],
            __WEBPACK_IMPORTED_MODULE_13__pages_post_details_post_details__["a" /* PostDetailsPage */],
            __WEBPACK_IMPORTED_MODULE_14__pages_visit_visit__["a" /* VisitPage */],
            __WEBPACK_IMPORTED_MODULE_15__pages_slides_slides__["a" /* SlidesPage */],
        ],
        imports: [
            __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["a" /* BrowserModule */],
            __WEBPACK_IMPORTED_MODULE_5__angular_http__["c" /* HttpModule */],
            __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["e" /* IonicModule */].forRoot(__WEBPACK_IMPORTED_MODULE_9__app_component__["a" /* MyApp */]),
            __WEBPACK_IMPORTED_MODULE_6__angular_common__["CommonModule"],
            __WEBPACK_IMPORTED_MODULE_7_ng2_file_upload__["FileUploadModule"]
        ],
        bootstrap: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["c" /* IonicApp */]],
        entryComponents: [
            __WEBPACK_IMPORTED_MODULE_9__app_component__["a" /* MyApp */],
            __WEBPACK_IMPORTED_MODULE_10__pages_home_home__["a" /* HomePage */],
            __WEBPACK_IMPORTED_MODULE_11__pages_my_post_my_post__["a" /* MyPostPage */],
            __WEBPACK_IMPORTED_MODULE_12__pages_edit_post_edit_post__["a" /* EditPostPage */],
            __WEBPACK_IMPORTED_MODULE_13__pages_post_details_post_details__["a" /* PostDetailsPage */],
            __WEBPACK_IMPORTED_MODULE_14__pages_visit_visit__["a" /* VisitPage */],
            __WEBPACK_IMPORTED_MODULE_15__pages_slides_slides__["a" /* SlidesPage */],
        ],
        providers: [
            __WEBPACK_IMPORTED_MODULE_4__ionic_native_status_bar__["a" /* StatusBar */],
            __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__["a" /* SplashScreen */],
            { provide: __WEBPACK_IMPORTED_MODULE_1__angular_core__["ErrorHandler"], useClass: __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["d" /* IonicErrorHandler */] },
            { provide: 'postService', useClass: __WEBPACK_IMPORTED_MODULE_8__providers_post_service_post_service__["a" /* PostServiceProvider */] },
            { provide: 'urls', useClass: __WEBPACK_IMPORTED_MODULE_17__providers_urls_urls__["a" /* UrlsProvider */] }
        ]
    })
], AppModule);

//# sourceMappingURL=app.module.js.map

/***/ }),

/***/ 273:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PostServiceProvider; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__(197);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__ = __webpack_require__(274);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var PostServiceProvider = (function () {
    function PostServiceProvider(http) {
        this.http = http;
        // 获取 urls
        this.urls = JSON.parse(window.localStorage.getItem('urls'));
        // 获取我的信息
        this.myInfo = window['myInfo'];
        // 定义 headers
        this.headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["a" /* Headers */]({ 'Content-Type': 'application/json' });
        console.log('启动 post 服务');
    }
    // 获取帖子列表
    PostServiceProvider.prototype.getPostList = function (pageNum) {
        var url = this.urls.path + this.urls.neighborsLis + "?page=" + pageNum;
        console.log(url);
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // 获取"我的"帖子列表
    PostServiceProvider.prototype.getMyPostList = function (ownerId, pageNum) {
        var url = this.urls.path + this.urls.myPostList + '?ownerId=' + ownerId + '&page=' + pageNum;
        console.log(url);
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // 获取帖子详情
    PostServiceProvider.prototype.getPostDetails = function (postId) {
        var url = this.urls.path + this.urls.postDetails + '?subjectid=' + postId;
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // 给帖子点赞
    PostServiceProvider.prototype.giveThumb = function (postId, ownerId) {
        var url = this.urls.path + this.urls.giveThumb + '?postid=' + postId + '&ownerid=' + ownerId;
        // console.log(url);
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // 发帖
    PostServiceProvider.prototype.addPost = function (text, pics) {
        var url = this.urls.path + this.urls.addPost;
        var body = "publishId=" + this.myInfo.id + "&body=" + text + "&pics=" + pics;
        var postHeaders = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["a" /* Headers */]({ 'Content-Type': 'application/x-www-form-urlencoded' });
        return this.http.post(url, body, { headers: postHeaders })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // 删帖
    PostServiceProvider.prototype.deletePost = function (postId) {
        var url = this.urls.path + this.urls.deletePost + '?subjectid=' + postId;
        console.log(url);
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // 回帖
    PostServiceProvider.prototype.addComment = function (replyid, subjectId, targetId, commentContent) {
        var url = this.urls.path + this.urls.addComment +
            '?body=' + commentContent + '&replyid=' + this.myInfo.id + '&subjectid=' + subjectId + '&targetid=' + targetId;
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    PostServiceProvider.prototype.handleError = function (error) {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    };
    return PostServiceProvider;
}());
PostServiceProvider = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Http */]])
], PostServiceProvider);

//# sourceMappingURL=post.service.js.map

/***/ }),

/***/ 276:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MyApp; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__ = __webpack_require__(196);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__ = __webpack_require__(193);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__pages_home_home__ = __webpack_require__(201);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};





var MyApp = (function () {
    function MyApp(platform, statusBar, splashScreen, urlsData) {
        this.urlsData = urlsData;
        this.rootPage = __WEBPACK_IMPORTED_MODULE_4__pages_home_home__["a" /* HomePage */];
        platform.ready().then(function () {
            // Okay, so the platform is ready and our plugins are available.
            // Here you can do any higher level native things you might need.
            statusBar.styleDefault();
            splashScreen.hide();
        });
        // 获取并存储"我的信息" http://192.168.0.250:8100/#id=1001&nickname=id-1001
        var hash = window.location.hash;
        window['myInfo'] = {};
        hash = hash.substring(1);
        var arr = hash.split('&');
        arr.forEach(function (v) {
            var temp = v.split('=');
            window['myInfo'][temp[0]] = temp[1];
        });
        // console.log( JSON.parse(window.localStorage.getItem('myInfo')) );
        console.log(window['myInfo']);
        /*  window['myInfo'] = {
          id: '1001',
          nickname: 'id-1001'
        }; */
        // alert('id: ' + window['myInfo']['id'] + ',  nickname: ' + window['myInfo']['nickname']);
        // 存储 myInfo
        // console.log('存储 myInfo');
        // window['myInfo'] && window.localStorage.setItem('myInfo', JSON.stringify(window['myInfo']));
        // console.log(window['myInfo'].id);
        // 存储 urls
        window.localStorage.setItem('urls', JSON.stringify(this.urlsData.urls));
        // console.log( JSON.parse(window.localStorage.getItem('urls')) );
    }
    return MyApp;
}());
MyApp = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({template:/*ion-inline-start:"E:\Development\neighbors\Code\src\app\app.html"*/'<ion-nav [root]="rootPage"></ion-nav>\n'/*ion-inline-end:"E:\Development\neighbors\Code\src\app\app.html"*/
    }),
    __param(3, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('urls')),
    __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* Platform */], __WEBPACK_IMPORTED_MODULE_2__ionic_native_status_bar__["a" /* StatusBar */], __WEBPACK_IMPORTED_MODULE_3__ionic_native_splash_screen__["a" /* SplashScreen */], Object])
], MyApp);

//# sourceMappingURL=app.component.js.map

/***/ }),

/***/ 277:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return EditPostPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};



var EditPostPage = EditPostPage_1 = (function () {
    function EditPostPage(navCtrl, navParams, service, actionSheetCtrl, alertCtrl, toastCtrl) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.service = service;
        this.actionSheetCtrl = actionSheetCtrl;
        this.alertCtrl = alertCtrl;
        this.toastCtrl = toastCtrl;
        // B: 初始化定义uploader变量,用来配置input中的uploader属性
        /*public uploader: FileUploader = new FileUploader({
          // url: "http://192.168.0.250:3000/uploadFile",
          url: "http://192.168.0.99:8080/api/postmanagement/addpost.action",
          method: "POST",
          itemAlias: "uploadedfile"
        });*/
        // C: 定义事件，选择文件
        /*selectedFileOnChanged(event: any) {
          // 打印文件选择名称
          console.log(event.target.value);
        }*/
        // D: 定义事件，上传文件
        /*uploadFile() {
          // 上传
          this.uploader.queue[0].onSuccess = function (response, status, headers) {
            // 上传文件成功
            if (status == 200) {
              // 上传文件后获取服务器返回的数据
              let tempRes = JSON.parse(response);
              console.log(tempRes);
            } else {
              // 上传文件后获取服务器返回的数据错误
              alert("wrong");
              console.log(response);
              console.log(status);
              console.log(headers);
            }
          };
          this.uploader.queue[0].upload(); // 开始上传
        }*/
        // 获取我的信息
        this.myInfo = window['myInfo'];
        // 获取设备信息
        this.deviceSystem = (window.navigator.appVersion.indexOf('Android') > 0) ? 'Android' : 'iphone';
        // 底部屏幕滑出
        /*presentActionSheet(picture, camera, camcorder, microphone, multiple) {
          let actionSheet = this.actionSheetCtrl.create({
            title: '类型',
            buttons: [
              {
                text: '图片',
                handler: () => {
                  picture.click();
                }
              }, {
                text: '拍照',
                handler: () => {
                  camera.click();
                }
              }, {
                text: '摄像',
                handler: () => {
                  camcorder.click();
                }
              }, {
                text: '取消',
                role: 'cancel',
                handler: () => {
                  console.log('Cancel clicked');
                }
              }
            ]
          });
          actionSheet.present();
        }*/
        // 回显视频
        /*private videoBox = [];
        showVideo(filesRef) {
          // 引用回显视频的数组
          let videoBox = this.videoBox;
          // 获得文件
          // console.log(files);
          const files: any = filesRef.files[0];
          // 判断文件大小
            // console.log(files.size);
          if (files.size > (10*1024*1024)) {
            alert('请选择少于 10M 的视频资源');
            return;
          }
          // 创建文件阅读器
          const reader = new FileReader();
          // 开始阅读
          // reader.readAsDataURL(files);
          reader.readAsDataURL(files);
          // 监听阅读完成事件
          reader.onload = function () {
            // console.log(reader.result);
            videoBox.push(reader.result);
          }
      
        }*/
        this.imageList = [];
        this.imageListData = [];
        // 发帖
        this.textareaContent = '';
    }
    EditPostPage.prototype.ionViewDidLoad = function () {
        console.log('我是: ' + this.myInfo.nickname + ', 我要发帖。');
    };
    // 返回
    EditPostPage.prototype.back = function () {
        var _this = this;
        if (this.imageList.length === 0 && this.textareaContent === '') {
            this.navCtrl.pop(EditPostPage_1);
            return;
        }
        var confirm = this.alertCtrl.create({
            title: '要离开编辑页面吗？',
            message: '已编辑的内容将会丢失。',
            buttons: [
                {
                    text: '留下',
                    handler: function () {
                        // console.log('留下');
                    }
                },
                {
                    text: '离开',
                    handler: function () {
                        console.log('确认离开');
                        _this.navCtrl.pop(EditPostPage_1);
                    }
                }
            ]
        });
        confirm.present();
    };
    // 定义图片处理函数
    EditPostPage.prototype.dealImgage = function (filesRef) {
        // 引用回显图片的数组
        var imageList = this.imageList;
        var imageListData = this.imageListData;
        // 获得文件
        // console.log(files);
        var files = filesRef.files[0];
        // 判断文件大小
        var imgSize = files.size / 1024 / 1024;
        console.log(imgSize);
        if (imgSize > 5) {
            alert('请选择少于 5M 的图片资源');
            return;
        }
        // 创建文件阅读器
        var reader = new FileReader();
        // 开始阅读
        // reader.readAsDataURL(files);
        reader.readAsDataURL(files);
        // 监听阅读完成事件
        reader.onload = function () {
            // 获取阅读结果
            var base64 = reader.result;
            var img = new Image();
            img.src = base64;
            img.onload = function () {
                var w = img.width;
                var h = img.height;
                var scale = w / h;
                w = w > 360 ? 360 : w;
                console.log(w);
                // if (imgSize >= 1 && imgSize < 2) {
                //   w /= 2;
                // } else if (imgSize >= 2 && imgSize < 3) {
                //   w /= 3;
                // } else if (imgSize >= 3 && imgSize < 4) {
                //   w /= 4;
                // } else if (imgSize >= 4) {
                //   w /= 4;
                // }
                h = w / scale || h;
                var quality = 0.8;
                // if (imgSize < 0.5) {
                //   quality = 0.5;
                // }else if (imgSize < 1) {
                //   quality = 0.45;
                // }else if (imgSize < 1.5) {
                //   quality = 0.4;
                // }else if (imgSize < 2) {
                //   quality = 0.35;
                // } else if (imgSize < 2.5) {
                //   quality = 0.3;
                // }else if (imgSize < 3) {
                //   quality = 0.25;
                // }else if (imgSize < 3.5) {
                //   quality = 0.2;
                // }else if (imgSize < 4) {
                //   quality = 0.15;
                // } else {
                //   quality = 0.1;
                // }
                var canvas = document.createElement("canvas");
                var ctx = canvas.getContext('2d');
                canvas.width = w;
                canvas.height = h;
                ctx.drawImage(img, 0, 0, w, h);
                console.log('quality: ' + quality);
                base64 = canvas.toDataURL('image/jpeg', quality);
                console.log("压缩后：" + base64.length / 1024 + "   " + 'base64');
                // 添加到回显图片的数组
                imageList.push(base64);
                // 截取替换
                base64 = base64.replace(/.*;base64,/, '');
                // 添加到图片数据数组
                imageListData.push(base64);
            };
        };
    };
    // 移除图片
    EditPostPage.prototype.removeImage = function (i) {
        this.imageList = this.imageList.slice(0, i).concat(this.imageList.slice(i + 1));
        this.imageListData = this.imageListData.slice(0, i).concat(this.imageListData.slice(i + 1));
    };
    // 定义 Toast 函数
    EditPostPage.prototype.showToast = function (message) {
        var toast = this.toastCtrl.create({
            message: message,
            duration: 1000,
            position: 'top'
        });
        toast.present(toast);
    };
    EditPostPage.prototype.presentPost = function () {
        var _this = this;
        if (this.textareaContent.trim() === '') {
            this.showToast('输入的内容不能为空');
            return;
        }
        ;
        this.service.addPost(this.textareaContent, this.imageListData)
            .then(function (res) {
            console.log(res);
            if (res.resultCode === '0') {
                window.localStorage.setItem('isAddPost', 'yes');
                _this.navCtrl.pop(EditPostPage_1);
            }
        });
    };
    return EditPostPage;
}());
EditPostPage = EditPostPage_1 = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'page-edit-post',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\pages\edit-post\edit-post.html"*/'<!--\n  Generated template for the EditPostPage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header>\n  <ion-item color="primary" no-lines>\n\n    <button class="nav-btn" ion-button clear color="light" icon-only (click)="back()">\n      <ion-icon name="ios-arrow-back"></ion-icon>\n    </button>\n\n    <ion-title text-center>发帖</ion-title>\n\n  </ion-item>\n</ion-header>\n\n\n<ion-content>\n  <!--<form #formRef="ngForm" (ngSubmit)="addPost(formRef.value)" enctype="multipart/form-data">-->\n\n    <div class="textarea-box">\n      <textarea maxlength="240" #textareaRef [(ngModel)]="textareaContent" placeholder="说点什么吧..."></textarea>\n      <span float-right>{{textareaContent.length}} / 240 </span>\n      <!--<span float-right>您还能输入：{{240 - textareaContent.length}} 字</span>-->\n    </div>\n\n    <ion-grid class="img-boxes">\n      <ion-row>\n        <ion-col class="img-box" col-4 *ngFor="let img of imageList; let i = index">\n          <div class="post-img">\n            <ion-icon name="close-circle" (click)="removeImage(i)"></ion-icon>\n            <div #addBoxRef [ngStyle]="{\'background-image\': \'url(\'+img+\')\', \'height\': addBoxRef.clientWidth + \'px\'}"></div>\n          </div>\n        </ion-col>\n        <ion-col class="img-box" col-4 *ngIf="imageList.length < 9">\n          <div #addBoxRef class="add-img" [ngStyle]="{\'height\': addBoxRef.clientWidth + \'px\'}">\n            <ion-icon name="ios-add-outline" class="add-sign" [ngStyle]="{\'font-size\': addBoxRef.clientWidth + \'px\'}"></ion-icon>\n            <!--<div (click)="presentActionSheet(picture, camera, camcorder, microphone)"></div>-->\n            <input type="file" *ngIf="deviceSystem === \'Android\'" accept="image/*" capture="camera" (change)="dealImgage(filesRef)" #filesRef>\n            <input type="file" *ngIf="deviceSystem === \'iphone\'" accept="image/*" (change)="dealImgage(filesRef)" #filesRef>\n          </div>\n        </ion-col>\n      </ion-row>\n    </ion-grid>\n\n    <hr>\n\n    <div margin-horizontal>\n      <button ion-button block [color]="(textareaContent === \'\') ? \'light\' : \'secondary\'" class="btn-submit" (tap)="presentPost()">发表</button>\n    </div>\n\n    <!--<input multiple type="file" ng2FileSelect [uploader]="uploader" (change)="selectedFileOnChanged($event)" />-->\n\n    <!--<button (click)="uploadFile()">提交</button>-->\n\n    <!--<img *ngFor="let img of test"  [src]="img" alt="">-->\n\n    <!--<input class="disappear" (change)="dealImgage(picture)" type="file" accept="image/*" #picture>-->\n    <!--<input class="disappear" (change)="dealImgage(camera)" type="file" accept="image/*" capture="camera" #camera>-->\n    <!--<input class="disappear" (change)="showVideo(camcorder)" type="file" accept="video/*" capture="camcorder" #camcorder>-->\n  <!--</form>-->\n</ion-content>'/*ion-inline-end:"E:\Development\neighbors\Code\src\pages\edit-post\edit-post.html"*/,
    }),
    __param(2, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('postService')),
    __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */],
        __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */], Object, __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* ActionSheetController */],
        __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["b" /* AlertController */],
        __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ToastController */]])
], EditPostPage);

var EditPostPage_1;
//# sourceMappingURL=edit-post.js.map

/***/ }),

/***/ 278:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ThumbComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};


var ThumbComponent = (function () {
    function ThumbComponent(service, toastCtrl) {
        this.service = service;
        this.toastCtrl = toastCtrl;
        // 从父组件注入帖子ID
        this.postId = '';
        // 获取当前用户的ID
        this.ownerId = window['myInfo'].id;
        console.log('Hello ThumbComponent Component');
    }
    // 定义 Toast 函数
    ThumbComponent.prototype.showToast = function (position) {
        if (position === void 0) { position = 'top'; }
        var toast = this.toastCtrl.create({
            message: '抱歉，您已点过赞了哦！',
            duration: 1000,
            position: position
        });
        toast.present(toast);
    };
    // 给帖子点赞
    ThumbComponent.prototype.giveThumb = function () {
        var _this = this;
        this.service.giveThumb(this.postId, this.ownerId)
            .then(function (res) {
            if (res.resultCode === '0') {
                _this.thumbCount++;
                var temp = JSON.parse(window.localStorage.getItem('recordThumb'));
                temp.push({
                    id: _this.postId,
                    count: _this.thumbCount
                });
                window.localStorage.setItem('recordThumb', JSON.stringify(temp));
                return;
            }
            _this.showToast();
        });
    };
    return ThumbComponent;
}());
__decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
    __metadata("design:type", Object)
], ThumbComponent.prototype, "postId", void 0);
__decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
    __metadata("design:type", Number)
], ThumbComponent.prototype, "thumbCount", void 0);
ThumbComponent = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'thumb',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\components\thumb\thumb.html"*/'<button ion-button clear icon-left (click)="giveThumb()">\n  <ion-icon name="thumbs-up"></ion-icon>\n  <div>{{thumbCount}} 赞</div>\n</button>'/*ion-inline-end:"E:\Development\neighbors\Code\src\components\thumb\thumb.html"*/
    }),
    __param(0, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('postService')),
    __metadata("design:paramtypes", [Object, __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ToastController */]])
], ThumbComponent);

//# sourceMappingURL=thumb.js.map

/***/ }),

/***/ 279:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return UrlsProvider; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
/* 此文档用于测试与发布 */

var UrlsProvider = (function () {
    function UrlsProvider() {
        this.urls = {
             path: 'http://120.24.37.216:8080/xiandao/api/',
             baseUrl: 'http://120.24.37.216:8080/xiandao/',
            // 贺刚
//             path: 'http://192.168.0.99:8080/api/',
//             baseUrl: 'http://192.168.0.99:8080/',
//            path: 'http://183.214.91.132:8081/xiandao/api/',
//            baseUrl: 'http://183.214.91.132:8081/xiandao/',
            // 长房
            // path: 'http://cfl.chanfine.com:8080/cfl/api/',
            // baseUrl: 'http://cfl.chanfine.com:8080/cfl/',
            // 先导
            // path: 'http://183.214.91.132:8081/xiandao/api/',
            // baseUrl: 'http://183.214.91.132:8081/xiandao/',
            // 获取所有帖子
            neighborsLis: 'postmanagement/getAllSubjetListBySubject.action',
            // 帖子点赞接口
            giveThumb: 'postmanagement/addfabulous.action',
            // 新增帖子接口
            addPost: 'postmanagement/addpost.action',
            // 删除帖子接口
            deletePost: 'postmanagement/deleteSubjectById.action',
            // 新增评论（回帖）接口
            addComment: 'postmanagement/addcomment.action',
            // 我的帖子接口
            myPostList: 'postmanagement/getSubjectByUserId.action',
            // 帖子详情接口
            postDetails: 'postmanagement/getSubjectDetail.action'
        };
    }
    return UrlsProvider;
}());
UrlsProvider = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __metadata("design:paramtypes", [])
], UrlsProvider);

//# sourceMappingURL=urls.js.map

/***/ }),

/***/ 280:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DeletePostComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};


var DeletePostComponent = (function () {
    function DeletePostComponent(service, alertCtrl) {
        this.service = service;
        this.alertCtrl = alertCtrl;
        // 接收父组件的帖子ID
        this.postId = '';
        // 告知父组件，其自身已被删除
        this.deleteData = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
    }
    // 定义确定删帖对话框
    DeletePostComponent.prototype.showConfirm = function () {
        var _this = this;
        var confirm = this.alertCtrl.create({
            title: '确认删除',
            message: '确认删除这个帖子吗？',
            buttons: [
                {
                    text: '取消',
                    handler: function () {
                        // console.log('取消删除');
                    }
                },
                {
                    text: '确认',
                    handler: function () {
                        // console.log('确认删除');
                        _this.deletePost();
                    }
                }
            ]
        });
        confirm.present();
    };
    DeletePostComponent.prototype.deletePost = function () {
        var _this = this;
        this.service.deletePost(this.postId).
            then(function (res) {
            if (res.resultCode === '0') {
                _this.deleteData.emit(true);
            }
        });
    };
    return DeletePostComponent;
}());
__decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
    __metadata("design:type", Object)
], DeletePostComponent.prototype, "postId", void 0);
__decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Output"])(),
    __metadata("design:type", Object)
], DeletePostComponent.prototype, "deleteData", void 0);
DeletePostComponent = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'delete-post',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\components\delete-post\delete-post.html"*/'<button ion-button clear icon-only (click)="showConfirm()">\n  <ion-icon name="md-trash"></ion-icon>\n</button>'/*ion-inline-end:"E:\Development\neighbors\Code\src\components\delete-post\delete-post.html"*/
    }),
    __param(0, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('postService')),
    __metadata("design:paramtypes", [Object, __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["b" /* AlertController */]])
], DeletePostComponent);

//# sourceMappingURL=delete-post.js.map

/***/ }),

/***/ 40:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SlidesPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


/**
 * Generated class for the SlidesPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
var SlidesPage = SlidesPage_1 = (function () {
    function SlidesPage(navCtrl, navParams) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        // 获取 baseUrl
        this.baseUrl = JSON.parse(window.localStorage.getItem('urls')).baseUrl;
    }
    SlidesPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad SlidesPage');
        // 获取幻灯片数组
        this.imageList = this.navParams.data.imageList;
        // 获取幻灯片初始索引
        this.initialSlide = this.navParams.data.index;
    };
    // 返回
    SlidesPage.prototype.back = function () {
        this.navCtrl.pop(SlidesPage_1);
    };
    return SlidesPage;
}());
SlidesPage = SlidesPage_1 = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'page-slides',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\pages\slides\slides.html"*/'<!--\n  Generated template for the SlidesPage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header class="header" color="primary">\n  <ion-item color="primary" no-lines>\n\n    <button class="nav-btn" ion-button clear color="light" icon-only (click)="back()">\n      <ion-icon name="ios-arrow-back"></ion-icon>\n    </button>\n\n    <ion-title text-center>查看图片</ion-title>\n\n  </ion-item>\n</ion-header>\n\n\n<ion-content class="contant" padding>\n\n  <ion-slides pager loop spaceBetween=10 [initialSlide]="initialSlide">\n    <ion-slide *ngFor="let img of imageList">\n      <img [src]="baseUrl + img.url" alt="">\n    </ion-slide>\n  </ion-slides>\n\n</ion-content>'/*ion-inline-end:"E:\Development\neighbors\Code\src\pages\slides\slides.html"*/,
    }),
    __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */]])
], SlidesPage);

var SlidesPage_1;
//# sourceMappingURL=slides.js.map

/***/ }),

/***/ 50:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PostDetailsPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__my_post_my_post__ = __webpack_require__(102);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__visit_visit__ = __webpack_require__(103);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__slides_slides__ = __webpack_require__(40);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};





var PostDetailsPage = PostDetailsPage_1 = (function () {
    function PostDetailsPage(navCtrl, navParams, loadingCtrl, service, toastCtrl) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.loadingCtrl = loadingCtrl;
        this.service = service;
        this.toastCtrl = toastCtrl;
        // 获取 baseUrl
        this.baseUrl = JSON.parse(window.localStorage.getItem('urls')).baseUrl;
        // 获取我的信息
        this.myInfo = window['myInfo'];
        // 定义幻灯片数组
        this.slidersImageList = [];
        // 定义回复的对象（非评论！！！）
        this.targetId = '';
        this.targetName = '';
        // 评论内容
        this.commentContent = '';
    }
    // 获取帖子列表
    PostDetailsPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        // 定义遮罩对象
        var loader = this.loadingCtrl.create({
            content: "请稍等..."
        });
        loader.present();
        this.service.getPostDetails(this.navParams.data.postId)
            .then(function (resRes) {
            _this.postData = resRes.data;
            console.log(resRes.data);
            loader.dismiss();
        });
    };
    PostDetailsPage.prototype.ionViewDidLoad = function () {
        console.log('我是: ' + this.myInfo.nickname + ', 我在看 帖子 id 为：' + this.navParams.data.postId + ' 的帖子详情。');
    };
    // 返回
    PostDetailsPage.prototype.back = function () {
        this.navCtrl.pop(PostDetailsPage_1);
    };
    // 我的帖子
    PostDetailsPage.prototype.myPost = function () {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_2__my_post_my_post__["a" /* MyPostPage */]);
    };
    // 拜访邻居
    PostDetailsPage.prototype.visitNeighbor = function (publishName, publishId) {
        // 自己拜访自己
        if (publishId == this.myInfo.id) {
            this.myPost();
            return;
        }
        // 拜访邻居
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_3__visit_visit__["a" /* VisitPage */], {
            hostInfo: {
                nickname: publishName,
                id: publishId
            }
        });
    };
    // 定义 Toast 函数
    PostDetailsPage.prototype.showToast = function (message) {
        var toast = this.toastCtrl.create({
            message: message,
            duration: 1000,
            position: 'top'
        });
        toast.present(toast);
    };
    // 获得焦点，将回复的对象清空
    /*getFocus() {
      console.log('清空回复对象信息');
    }*/
    // 失去焦点，清除评论/回复的内容
    /*loseFocus(commentBox) {
      this.commentContent= '';
      commentBox.placeholder = '评论';
    }*/
    // 添加评论
    PostDetailsPage.prototype.addComment = function (commentBox) {
        this.commentContent = '';
        commentBox.focus();
        commentBox.placeholder = '评论';
        this.targetId = '';
        this.targetName = '';
    };
    // 设置回复对象
    PostDetailsPage.prototype.setTarget = function (commentBox, replyId, replyName) {
        this.commentContent = '';
        commentBox.focus();
        commentBox.placeholder = '回复 ' + replyName + ':';
        this.targetId = replyId;
        this.targetName = replyName;
    };
    // 提交评论
    PostDetailsPage.prototype.submitComent = function (commentBox) {
        var _this = this;
        console.log('我是' + this.myInfo.nickname + ', 我要评论/回复的 帖子ID 与 用户ID 分别是：' + this.postData.id + '、' + this.targetId);
        console.log(this.commentContent);
        var commentContent = this.commentContent.trim();
        if (commentContent === '') {
            this.showToast('输入的内容不能为空');
            return;
        }
        ;
        this.service.addComment(this.myInfo.id, this.postData.id, this.targetId, commentContent)
            .then(function (res) {
            console.log(res);
            if (res.resultCode === '0') {
                _this.postData.comments.push({
                    "body": commentContent,
                    "replyid": _this.myInfo.id,
                    "replyName": _this.myInfo.nickname,
                    "targetid": _this.targetId || null,
                    "targetName": _this.targetName || null
                });
                _this.commentContent = '';
                commentBox.placeholder = '评论';
                _this.targetId = '';
                _this.targetName = '';
            }
        });
    };
    // 删除
    PostDetailsPage.prototype.del = function () {
        console.log('删除功能暂未实现!');
    };
    // 打开幻灯片
    PostDetailsPage.prototype.goSlides = function (imageList, index) {
        this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_4__slides_slides__["a" /* SlidesPage */], {
            imageList: imageList,
            index: index
        });
    };
    return PostDetailsPage;
}());
PostDetailsPage = PostDetailsPage_1 = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'page-post-details',template:/*ion-inline-start:"E:\Development\neighbors\Code\src\pages\post-details\post-details.html"*/'<ion-header>\n  <ion-item color="primary" no-lines>\n\n    <button class="nav-btn" ion-button clear color="light" icon-only (click)="back()">\n      <ion-icon name="ios-arrow-back"></ion-icon>\n    </button>\n\n    <ion-title text-center>详情</ion-title>\n\n    <!--<button item-right class="nav-btn" ion-button clear color="light" large>\n      <ion-icon name="md-share"></ion-icon>\n    </button>-->\n\n  </ion-item>\n</ion-header>\n\n\n<ion-content>\n\n  <ion-card *ngIf="postData">\n\n    <ion-item (click)="visitNeighbor(postData.publishName, postData.publishId)">\n      <ion-avatar item-start item-left>\n        <img [src]="postData.avatar ? (baseUrl + postData.avatar) : \'./assets/images/default-avatar.png\'">\n      </ion-avatar>\n      <h2 item-left>\n        {{postData.publishName}}\n      </h2>\n      <p item-right>\n        {{postData.publishTime}}\n      </p>\n    </ion-item>\n\n    <ion-card-content>\n      <p>\n        {{postData.body}}\n      <ion-grid *ngIf="postData.imageList">\n        <ion-row>\n          <ion-col col-4 *ngFor="let img of postData.imageList; let i = index">\n            <div #imgBoxRef (click)="goSlides(postData.imageList, i)" class="post-img" [ngStyle]="{\'background-image\': \'url(\'+baseUrl+img.url+\')\', \'height\': imgBoxRef.clientWidth + \'px\'}"></div>\n          </ion-col>\n        </ion-row>\n      </ion-grid>\n    </ion-card-content>\n\n    <div float-right>\n      <!--<thumb [postId]="postData.id" [thumbCount]="postData.upnumber" [postType]="postType"></thumb>-->\n      <thumb [postId]="postData.id" [thumbCount]="postData.upnumber"></thumb>\n      <!--<delete-post *ngIf="postData.publishId == myInfo.id" [postId]="postData.id"></delete-post>-->\n      <button ion-button clear icon-only (click)="addComment(commentBoxRef)"><ion-icon name="create"></ion-icon></button>\n    </div>\n\n  </ion-card>\n\n  <ion-list no-lines class="commentList" *ngIf="postData?.comments">\n\n    <ion-item *ngFor="let c of postData.comments">\n\n      <p *ngIf="c.targetid === null">\n        <span  class="nickname" (tap)="visitNeighbor(c.replyName, c.replyid)">\n          {{c.replyName}}\n        </span>:\n        <span text-wrap (click)="setTarget(commentBoxRef, c.replyid, c.replyName)" (press)="del()">\n          {{c.body}}\n        </span>\n      </p>\n\n      <p *ngIf="c.targetid">\n        <span  class="nickname" (tap)="visitNeighbor(c.replyName, c.replyid)">\n          {{c.replyName}}\n        </span>回复\n        <span  class="nickname" (tap)="visitNeighbor(c.targetName,c.targetid)">\n          {{c.targetName}}\n        </span>:\n        <span text-wrap (click)="setTarget(commentBoxRef, c.replyid, c.replyName)"  (press)="del()">\n          {{c.body}}\n        </span>\n        <br>\n      </p>\n\n    </ion-item>\n\n  </ion-list>\n\n<div class="footer">\n  <input #commentBoxRef #commentBoxValid="ngModel" required [(ngModel)]="commentContent" type="text" placeholder="评论">\n  <button ion-button [color]="commentBoxValid.errors?.required ? \'light\' : \'secondary\'" (tap)="submitComent(commentBoxRef)">发送</button>\n</div>\n\n</ion-content>\n'/*ion-inline-end:"E:\Development\neighbors\Code\src\pages\post-details\post-details.html"*/,
    }),
    __param(3, Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])('postService')),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* NavController */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* NavParams */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* LoadingController */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* LoadingController */]) === "function" && _c || Object, Object, typeof (_d = typeof __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ToastController */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ToastController */]) === "function" && _d || Object])
], PostDetailsPage);

var PostDetailsPage_1, _a, _b, _c, _d;
//# sourceMappingURL=post-details.js.map

/***/ })

},[202]);
//# sourceMappingURL=main.js.map