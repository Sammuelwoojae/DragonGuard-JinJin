//
//  CompareViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/27.
//

import Foundation
import RxSwift
import RxCocoa

final class CompareViewModel{
    static let viewModel = CompareViewModel()
    var firstRepo = ""
    var secondRepo = ""
    var repositryInfo: BehaviorSubject<[String]> = BehaviorSubject(value: [])
    var repoUserInfo: BehaviorSubject<CompareUserModel> = BehaviorSubject(value: CompareUserModel(firstResult: [], secondResult: []))
    var repo1Info: BehaviorSubject<[FirstRepoModel]> = BehaviorSubject(value: [])
    var repo2Info: BehaviorSubject<[secondRepoModel]> = BehaviorSubject(value: [])
    
    let service = CompareService()
    let disposeBag = DisposeBag()
    var sendData: BehaviorSubject<CompareUserModel> = BehaviorSubject(value: CompareUserModel(firstResult: [], secondResult: []))
    var compareUser: CompareUserModel?
    var compareRepo: CompareRepoModel?
    
    // 유저 정보 가져오는 함수
    func getContributorInfo(firstRepoName: String, secondRepoName: String) -> Observable<CompareUserModel>{
        self.firstRepo = firstRepoName
        self.secondRepo = secondRepoName
        return Observable.create(){ observer in
            self.service.beforeSendingInfo(firstRepo: firstRepoName, secondRepo: secondRepoName)
                .subscribe(onNext: { contributorInfo in
                    observer.onNext(contributorInfo)
                    self.compareUser = contributorInfo
                    self.sendData.onNext(contributorInfo)
                    if !contributorInfo.firstResult.isEmpty && !contributorInfo.secondResult.isEmpty{

                    }
                    
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
    // 받아온 유저 정보를 CompareUserController 파일로 보냄
    func getUserInfo() -> Observable<CompareUserModel> {
        return Observable.create(){ observer in
            self.sendData.subscribe(onNext: { data in
                observer.onNext(data)
            }).disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
//    func start(){
//        self.service.getCompareInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
//            .subscribe(onNext: { repoInfo in
//                print("called")
//                print(repoInfo)
////                    observer.onNext(repoInfo)
//                self.compareRepo = repoInfo
//            })
//            .disposed(by: self.disposeBag)
//    }
    
    func getRepositoryInfo()-> Observable<CompareRepoModel>{
        return Observable.create { observer in
            self.service.getCompareInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
                .subscribe(onNext: { repoInfo in
                    print("called")
                    print(repoInfo)
//                    observer.onNext(repoInfo)
                    self.compareRepo = repoInfo
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create ()
        }
    }
    
    func getRepoInfo() -> Observable<CompareRepoModel>{
        
        return Observable.create(){ observer in
            self.service.getCompareInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
                .subscribe(onNext: { repoInfo in
                    print("called")
                    print(repoInfo)
                    observer.onNext(repoInfo)
                })
                .disposed(by: self.disposeBag)

//            if self.compareRepo != nil{
//                print("not nil here")
//                observer.onNext(self.compareRepo!)
//            }
            
            return Disposables.create()
        }
    }
    
    
}
