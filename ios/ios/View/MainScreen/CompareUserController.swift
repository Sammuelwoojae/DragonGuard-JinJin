//
//  CompareGraphController.swift
//  ios
//
//  Created by 홍길동 on 2023/02/21.
//

import Foundation
import UIKit
import SnapKit
import RxCocoa
import RxSwift
import Charts


final class CompareUserController : UIViewController, SendingProtocol {
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    var repoUserInfo: CompareUserModel = CompareUserModel(firstResult: [], secondResult: [])
    let viewModel = CompareViewModel()
    let disposeBag = DisposeBag()
    var chooseArray: [String] = []
    var user1Index: Int?
    var user2Index: Int?
    var lastIndexOfFisrtArray: Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addToView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getUserInfo()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var user1Button : UIButton = {
        let btn = UIButton()
        btn.setTitle("choice user1", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.textAlignment = .center
        btn.layer.borderWidth = 2
        btn.layer.cornerRadius = 20
        btn.addTarget(self, action: #selector(clickedChooseUser1), for: .touchUpInside)
        return btn
    }()
    
    lazy var user1ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .red
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var user2Button : UIButton = {
        let btn = UIButton()
        btn.setTitle("choice user2", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.textAlignment = .center
        btn.layer.borderWidth = 2
        btn.layer.cornerRadius = 20
        btn.addTarget(self, action: #selector(clickedChooseUser2), for: .touchUpInside)
        return btn
    }()
    
    lazy var user2ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .blue
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var chartCommit : BarChartView = {
        let chart1 = BarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    lazy var chartAddDel : BarChartView = {
        let chart1 = BarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(user1Button)
        self.view.addSubview(user1ColorButton)
        self.view.addSubview(user2Button)
        self.view.addSubview(user2ColorButton)
        self.view.addSubview(chartCommit)
        self.view.addSubview(chartAddDel)
        setAutoLayout()
    }
    
    @objc func clickedChooseUser1(){
        
        let mvNext = CompareSelectedUserView()
        
        if chooseArray.count == 0{
            for data in self.repoUserInfo.firstResult{
                chooseArray.append(data.githubId)
            }
            self.lastIndexOfFisrtArray = chooseArray.count
            for data in self.repoUserInfo.secondResult{
                chooseArray.append(data.githubId)
            }
        }
        
        mvNext.userArray = chooseArray
        mvNext.whereComeFrom = "user1"
        mvNext.delegate = self
        self.present(mvNext, animated: true)
        
    }
    
    @objc func clickedChooseUser2(){
        let mvNext = CompareSelectedUserView()
        
        if chooseArray.count == 0{
            for data in self.repoUserInfo.firstResult{
                chooseArray.append(data.githubId)
            }
            self.lastIndexOfFisrtArray = chooseArray.count
            for data in self.repoUserInfo.secondResult{
                chooseArray.append(data.githubId)
            }
        }
        
        mvNext.userArray = chooseArray
        mvNext.whereComeFrom = "user2"
        mvNext.delegate = self
        self.present(mvNext, animated: true)
    }
    
    func dataSend(index: Int, user: String) {
        print(self.chooseArray)
        if user == "user1"{
            self.user1Index = index
            self.user1Button.setTitle(self.chooseArray[index], for: .normal)
        }
        else if user == "user2"{
            self.user2Index = index
            self.user2Button.setTitle(self.chooseArray[index], for: .normal)
        }
        
        if self.user1Index != 0 && self.user2Index != 0 && self.user1Index != nil && self.user2Index != nil{
            self.setChartCommit()
            self.setChartAddDel()
        }
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        user1Button.snp.makeConstraints ({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(30)
            make.trailing.equalTo(user1ColorButton.snp.leading).offset(-10)
            make.height.equalTo(deviceWidth/10)
        })
        
        user1ColorButton.snp.makeConstraints ({ make in
            //            make.top.equalTo(view.safeAreaLayoutGuide)
            make.centerY.equalTo(user1Button)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
        
        user2Button.snp.makeConstraints ({ make in
            make.top.equalTo(user1Button.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.height.equalTo(deviceWidth/10)
        })
        
        user2ColorButton.snp.makeConstraints ({ make in
            make.centerY.equalTo(user2Button)
            make.leading.equalTo(user2Button.snp.trailing).offset(10)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
        
        chartCommit.snp.makeConstraints ({ make in
            make.top.equalTo(user2Button.snp.bottom).offset(20)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(deviceHeight/3)
        })
        
        chartAddDel.snp.makeConstraints ({ make in
            make.top.equalTo(chartCommit.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        
    }
    
    func getUserInfo(){
        
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { timer in
            self.viewModel.bringUserInfo()
          
            self.viewModel.repoUserInfo.subscribe(onNext: {
                self.repoUserInfo = $0
            })
            .disposed(by: self.disposeBag)
            if self.repoUserInfo.firstResult.count != 0{
                timer.invalidate()
                
            }
        })
    }

    
}

extension CompareUserController : ChartViewDelegate {
    private func setChartCommit() {
        var dataSet : [BarChartDataSet] = []
        guard let lastIndexOfFisrtArray = self.lastIndexOfFisrtArray else {return}
        
        // data set 1
        var userInfo = [ChartDataEntry]()
        guard let user1Index = self.user1Index else {return}
        var set1 = BarChartDataSet()
        var repo = ""
        var newUser1Index = 0
        
        if lastIndexOfFisrtArray > user1Index{  // 첫 번째 유저 선택이 첫번쨰 배열 안에 있는 경우
            newUser1Index = user1Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user1Index{ // 첫 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser1Index = user1Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.firstResult[newUser1Index].commits))
            userInfo.append(dataEntry1)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.firstResult[newUser1Index].githubId)
        }
        else if repo == "second"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.secondResult[newUser1Index].commits))
            userInfo.append(dataEntry1)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.secondResult[newUser1Index].githubId)
        }
        
        dataSet.append(set1)
        
        
        // data set 2
        guard let user2Index = self.user2Index else {return}
        var newUser2Index = 0
        var set2 = BarChartDataSet()
        var userInfo2 = [ChartDataEntry]()
        repo = ""
        if lastIndexOfFisrtArray > user2Index{  // 두 번째 유저 선택이 첫번째 배열 안에 있는 경우
            newUser2Index = user2Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user2Index{ // 두 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser2Index = user2Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry2 = BarChartDataEntry(x: 1, y: Double(self.repoUserInfo.firstResult[newUser2Index].commits))
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.firstResult[newUser2Index].githubId)
        }
        else if repo == "second"{
            let dataEntry2 = BarChartDataEntry(x: 1, y: Double(self.repoUserInfo.secondResult[newUser2Index].commits))
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.secondResult[newUser2Index].githubId)
        }
        
        dataSet.append(set2)
        
        
        //        commitDataSet.colors = [UIColor(red: 255/255, green: 0, blue: 0, alpha: 0.3)]
        
        
        let data = BarChartData(dataSets: dataSet)
        chartCommit.data = data
        chartCommitAttribute()
    }
    
    private func chartCommitAttribute(){
        chartCommit.xAxis.enabled = false
        
    }
    
    private func setChartAddDel() {
        //        var dataSet : [BarChartDataSet] = []
        let AddDelDataSet = BarChartDataSet(
            entries: [
                BarChartDataEntry(x: 2, y: 110),
                BarChartDataEntry(x: 3, y: 120),
            ]
        )
        AddDelDataSet.colors = [UIColor(red: 255/255, green: 0, blue: 0, alpha: 0.3)]
        
        
        let data = BarChartData(dataSets: [AddDelDataSet])
        chartAddDel.data = data
        chartAddDelAttribute()
    }
    
    private func chartAddDelAttribute(){
        chartAddDel.xAxis.enabled = false
        
    }
}

/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreViewCompareUserGraphController:PreviewProvider {
    static var previews: some View {
        CompareUserController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewCompareUserGraphController2:PreviewProvider {
    static var previews: some View {
        CompareUserController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
