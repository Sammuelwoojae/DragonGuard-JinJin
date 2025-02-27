//
//  ConnectingBackend.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import RxCocoa
import RxSwift
import Foundation

final class SearchPageViewModel {
    static let viewModel = SearchPageViewModel()
    let searchPageService = SearchPageService()
    let disposeBag = DisposeBag()
    var pageCount = 1   //페이지 수
    
    // 검색 결과를 가져오는 함수
    func getSearchData(searchWord: String, type: String, change: Bool, filtering: String) -> Observable<[SearchResultModel]>{
        if change{
            self.pageCount = 1
        }
        return Observable.create(){ observer in
            self.searchPageService.getSearchResult(searchWord: searchWord,
                                                   page: self.pageCount,
                                                   type: type,
                                                   filtering: filtering)
                .subscribe(onNext: { searchResultList in
                    
                    observer.onNext(searchResultList)
                })
                .disposed(by: self.disposeBag)
            self.pageCount += 1
            
            return Disposables.create()
        }
    }
    
}


