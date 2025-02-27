//
//  SearchOraganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 조직 검색하는 클래스
final class SearchOraganizationService {
    
    
    func getOrganizationListService(name: String, type: String, page: Int, size: Int) -> Observable<[SearchOrganizationListModel]>{
        
        let url = APIURL.apiUrl.searchOrganizationList(ip: APIURL.ip,
                                                       name: name,
                                                       type: type,
                                                       page: page,
                                                       size: size)
        
        let encodedString = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        print(url)
        var result: [SearchOrganizationListModel] = []
        let access = UserDefaults.standard.string(forKey: "Access")

        return Observable.create(){ observer in
            AF.request(encodedString,
                       method: .get,
                       encoding: JSONEncoding.default,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<201)
            .responseData { response in
                switch response.result {
                case .success(let data):
                    do {
                        if let jsonArray = try JSONSerialization.jsonObject(with: data, options: []) as? [Any] {
                            
                            let jsonData = try JSONSerialization.data(withJSONObject: jsonArray,options: [])
                            
                            let object = try JSONDecoder().decode([SearchOrganizationListDecodingModel].self, from: jsonData)
                            
                            object.forEach { data in
                                result.append(SearchOrganizationListModel(id: data.id,
                                                                          name: data.name,
                                                                          type: data.organizationType,
                                                                          emailEndpoint: data.emailEndpoint))
                                
                            }
                        }
                    } catch {
                        print(error.localizedDescription)
                    }
                    
                    observer.onNext(result)
                case .failure(let error):
                    print(error.localizedDescription)
                }
            }
            
            return Disposables.create()
        }
    }
    
    
}
