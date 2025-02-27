//
//  LoginController.swift
//  ios
//
//  Created by 정호진 on 2023/02/22.
//

import Foundation
import UIKit
import SnapKit
import RxSwift
import SafariServices
import WebKit
import Alamofire

final class LoginController: UIViewController{
    let disposeBag = DisposeBag()
    var autoLoginCheck: Bool?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addUItoView()
        checkClearAuths()
        
        if autoLoginCheck ?? false{
            self.navigationController?.pushViewController(MainController(), animated: true)
        }
        
    }
    
    /*
     UI 코드 작성
     */
    
    // MARK:
    private lazy var fillView: UIView = {
        let view = UIView()
        view.backgroundColor = UIColor(red: 255/255, green: 104/255, blue: 120/255, alpha: 1.0) /* #ff6878 */
        return view
    }()
    
    // MARK:
    private lazy var imgView: UIImageView = {
        let imgView = UIImageView()
        imgView.image = UIImage(named: "2")
        return imgView
    }()
    
    private lazy var klipLoginBtn: CustomLoginButton = {
        let btn = CustomLoginButton()
        btn.inputData(icon: (UIImage(named: "KlipIcon")?.resize(newWidth: 40))!,
                      title: "Klip 로그인")
        btn.backgroundColor = UIColor(red: 45/255, green: 106/255, blue: 255/255, alpha: 1.0) /* #2d6aff */
        btn.clipsToBounds = true
        btn.layer.cornerRadius = 20
        btn.isEnabled = false
        btn.addTarget(self, action: #selector(clickedKlipLoginBtn), for: .touchUpInside)
        return btn
    }()
    
    private lazy var goGithubBtn: CustomLoginButton = {
        let btn = CustomLoginButton()
        btn.backgroundColor = UIColor(red: 27/255, green: 31/255, blue: 34/255, alpha: 1.0) /* #1b1f22 */
        btn.inputData(icon: (UIImage(named: "githubIcon")?.resize(newWidth: 40))!,
                      title: "Github 로그인")
        btn.clipsToBounds = true
        btn.layer.cornerRadius = 20
        btn.addTarget(self, action: #selector(clickedGoGihbubBtn), for: .touchUpInside)
        return btn
    }()
    
    /*
     UI Action 작성
     */
    
    @objc func clickedKlipLoginBtn(){
        LoginViewModel.loginService.prepareKlip()
            .subscribe(onNext: { url in
                self.moveToDeepLink(url)
            })
            .disposed(by: disposeBag)
    }
    
    // DeepLink 이동하는 함수
    private func moveToDeepLink(_ url: String){
        // Klip 앱 이동 후 다른 화면 출력
        let klipCheck = KlipLoginCheckView()
        klipCheck.viewModel = LoginViewModel.loginService
        self.present(klipCheck, animated: true)
        
        // 사용자 기본 브라우저에서 deeplink 주소 열 수 있는지 확인 후 열기
        if let url = URL(string: url) {
            // 사용자의 기본 브라우저에서 url 확인
            if UIApplication.shared.canOpenURL(url) {
                // 사용자 기본 브라우저에서 url 열기
                UIApplication.shared.open(url, options: [:]){ handler in
                    let configuration = SFSafariViewController.Configuration()
                    configuration.entersReaderIfAvailable = false
                    let deepLinkView = SFSafariViewController(url: url,configuration: configuration)
                    self.present(deepLinkView, animated: true)
                }
            } else {
                print("기본 브라우저를 열 수 없습니다.")
            }
        }
        
    }
    
    @objc func clickedGoGihbubBtn(){
        let url = URL(string: APIURL.apiUrl.callBackendForGithubLogin(ip: APIURL.ip))!
        print("url \(url)")
        let urlRequest = URLRequest(url: url)
        let vc = UIViewController()
//        let newViewController = UINavigationController(rootViewController: vc )
        
        let webView = WKWebView(frame: vc.view.bounds)
        
        vc.view.addSubview(webView)
        
        webView.navigationDelegate = self
        webView.goBack()
        webView.load(urlRequest)
        vc.modalPresentationStyle = .fullScreen
        self.present(vc,animated: true)
        
    }

    
    // 사용자가 인증을 완료했는지 확인하는 함수
    func checkClearAuths(){
        let checkGithubAuth = LoginViewModel.loginService.githubAuthSubject
        let checkklipAuth = LoginViewModel.loginService.klipAuthSubject
        
        Observable.combineLatest(checkGithubAuth, checkklipAuth)
            .subscribe(onNext: { first, second in
                if first && second{
                    
                    let rootView = MainController()
                    
//                    for (key, value) in UserDefaults.standard.dictionaryRepresentation() {
//                       print("\(key): \(value)")
//                     }

                    self.klipLoginBtn.isEnabled = false
                    self.goGithubBtn.isEnabled = true
                    self.klipLoginBtn.layer.opacity = 1
                    self.goGithubBtn.layer.opacity = 1
                    self.present(rootView, animated: true)
//                    self.navigationController?.pushViewController(rootView, animated: true)
                    
                }
                else if first{
                    self.goGithubBtn.layer.opacity = 0.4
                    self.klipLoginBtn.isEnabled = true
                    self.goGithubBtn.isEnabled = false
                }
                else if second{
                    self.klipLoginBtn.layer.opacity = 0.4
                    self.klipLoginBtn.isEnabled = false
                }
                else{
                    self.klipLoginBtn.isEnabled = false
                    self.goGithubBtn.isEnabled = true
                    self.goGithubBtn.layer.opacity = 1
                    self.klipLoginBtn.layer.opacity = 1
                }
            })
            .disposed(by: disposeBag)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        view.addSubview(klipLoginBtn)
        view.addSubview(goGithubBtn)
        view.addSubview(imgView)
        view.addSubview(fillView)
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        
        fillView.snp.makeConstraints { make in
            make.top.equalTo(0)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/4)
        }
        
        imgView.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/5)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/2)
        }
        
        klipLoginBtn.snp.makeConstraints({ make in
            make.top.equalTo(imgView.snp.bottom).offset(30)
            make.centerX.equalTo(view.snp.centerX)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/18)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/3)
        })
        
        goGithubBtn.snp.makeConstraints({ make in
            make.top.equalTo(klipLoginBtn.snp.bottom).offset(30)
            make.centerX.equalTo(view.snp.centerX)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/18)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/3)
        })
        
        
    }
    
}

extension LoginController: UIWebViewDelegate, WKNavigationDelegate, WKUIDelegate{
    
    // WKNavigationDelegate 메서드 구현
        func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction,
                     decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
            
            // 저장되어 있는 쿠키를 확인 구문
            WKWebsiteDataStore.default().httpCookieStore.getAllCookies { cookies in
                var accessTokenCheck = false
                var refreshTokenCheck = false
                
                for cookie in cookies{
                    print(cookie)
                    if cookie.name == "Access" {
                        UserDefaults.standard.set(cookie.value, forKey:"Access")
                        print("@@@ Access  저장하기: \(cookie.value)")
                        accessTokenCheck = true
                    }
                    if cookie.name == "Refresh" {
                        UserDefaults.standard.set(cookie.value, forKey:"Refresh")
                        print("@@@ Refresh  저장하기: \(cookie.value)")
                        refreshTokenCheck = true
                    }
               
                    if accessTokenCheck && refreshTokenCheck{
                        self.dismiss(animated: true)
//                        self.navigationController?.popViewController(animated: true)
                        LoginViewModel.loginService.githubAuthSubject.onNext(true)
                    }
                }
            }
          
            decisionHandler(.allow)
        }
    
}



