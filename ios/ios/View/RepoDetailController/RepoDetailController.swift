//
//  RepoDetailController.swift
//  ios
//
//  Created by 정호진 on 2023/05/31.
//

import Foundation
import UIKit
import SnapKit
import RxSwift
import Charts
import Lottie

final class RepoDetailController: UIViewController{
    private var userListCount: Int = 10
    private var userCommit: [Int] = []  // 사용자 커밋 횟수
    private var userName: [String] = [] // 사용자 깃허브 아이디
    private var dataColor:[[UIColor]] = []  // 랜덤 색상 설정
    var selectedTitle: String?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        addUIIndicator()
        view.backgroundColor = .white
        
        randomColor()
        testData()
//        addDetailUI()
    }
    
    // MARK: 로딩 UI
    private lazy var indicatorView: LottieAnimationView = {
        let view = LottieAnimationView(name: "graphLottie")
        view.center = self.view.center
        view.backgroundColor = .white
        view.loopMode = .loop
        return view
    }()
    
    // MARK: 스크롤 뷰
    private lazy var scrollView: UIScrollView = {
        let view = UIScrollView()
        return view
    }()
    
    // MARK: contentView
    private lazy var contentView: UIView = {
        let view = UIView()
        view.backgroundColor = .cyan
        return view
    }()
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        btn.addTarget(self, action: #selector(clickedBackBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 이중 View
    private lazy var customView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 20
        return view
    }()
    
    // MARK: Repository 그래프 기록
    private lazy var sparkLineView: SparkLineUIView = {
        let view = SparkLineUIView()
        view.backgroundColor = .clear
        return view
    }()
    
    // MARK: Repository title
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "???"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        label.textColor = .black
        return label
    }()
    
    // MARK: 유저 리스트 tableview
    private lazy var userListCollectionView: UICollectionView = {
        let layer = UICollectionViewFlowLayout()
        let collect = UICollectionView(frame: .zero, collectionViewLayout: layer)
        collect.backgroundColor = .white
        collect.isScrollEnabled = false
        return collect
    }()
    
    // MARK: 차트 그리는 UI
    private lazy var barChart: BarChartView = {
        let chart = BarChartView()
        chart.backgroundColor = .white
        return chart
    }()
    
    // MARK: 빈공간 채우는 뷰
    private lazy var blankView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
  
    
    /*
     Add UI and Set AutoLayout
     */
    
    // MARK: Add UI Indicator
    private func addUIIndicator(){
        self.view.addSubview(indicatorView)
        setIndicactorAutoLayout()
        indicatorView.play()
    }
    
    // MARK: Indicator View AutoLayout
    private func setIndicactorAutoLayout(){
        indicatorView.snp.makeConstraints { make in
            make.top.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
        }
    }
    
    // MARK: 로딩이 끝난 후 필요한 UI 추가
    private func addDetailUI(){
        self.view.addSubview(scrollView)
        
        scrollView.addSubview(contentView)
        
        contentView.addSubview(backBtn)
        contentView.addSubview(blankView)
        contentView.addSubview(customView)
        contentView.addSubview(sparkLineView)
        
        
        customView.addSubview(titleLabel)
        customView.addSubview(userListCollectionView)
        customView.addSubview(barChart)
        
        userListCollectionView.delegate = self
        userListCollectionView.dataSource = self
        userListCollectionView.register(UserListCollectionViewCell.self, forCellWithReuseIdentifier: UserListCollectionViewCell.identifier)
        
        
        setDeatilUIAutoLayout()
        
    }
    
    // MARK: 로딩이 끝난 후 필요한 UI AutoLayout
    private func setDeatilUIAutoLayout(){
        scrollView.snp.makeConstraints { make in
            make.top.leading.trailing.equalTo(self.view.safeAreaLayoutGuide)
            make.bottom.equalToSuperview()
        }
        
        // contentView
        contentView.snp.makeConstraints { make in
            make.top.equalTo(self.scrollView.snp.top)
            make.leading.equalTo(self.scrollView.snp.leading)
            make.trailing.equalTo(self.scrollView.snp.trailing)
            make.bottom.equalTo(self.scrollView.snp.bottom)
            make.width.equalTo(scrollView.snp.width)
        }
            
        // back Button
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top).offset(10)
            make.leading.equalTo(contentView.snp.leading).offset(10)
        }
        
        sparkLineView.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.bottom)
            make.leading.equalTo(contentView.snp.leading)
            make.trailing.equalTo(contentView.snp.trailing)
            make.height.equalTo(100).priority(250)
        }
        
        // Custom view
        customView.snp.makeConstraints { make in
            make.top.equalTo(sparkLineView.snp.centerY)
            make.leading.equalTo(self.contentView.snp.leading)
            make.trailing.equalTo(self.contentView.snp.trailing)
            make.bottom.equalTo(self.contentView.snp.bottom)
        }
        
        blankView.snp.makeConstraints { make in
            make.top.equalTo(customView.snp.centerY)
            make.leading.equalTo(customView.snp.leading)
            make.trailing.equalTo(customView.snp.trailing)
            make.bottom.equalToSuperview()
        }
        
        // Repository Title
        titleLabel.snp.makeConstraints { make in
            make.top.equalTo(sparkLineView.snp.bottom).offset(30)
            make.leading.equalTo(customView.snp.leading).offset(20)
        }
        
        // userlist tableview
        userListCollectionView.snp.makeConstraints { make in
            make.top.equalTo(titleLabel.snp.bottom).offset(30)
            make.leading.equalTo(customView.snp.leading).offset(30)
            make.trailing.equalTo(customView.snp.trailing).offset(-30)
            make.height.equalTo((Int(self.view.safeAreaLayoutGuide.layoutFrame.height)*userListCount/10)+30)
        }
        
        // 유저 커밋 그래프
        barChart.snp.makeConstraints { make in
            make.top.equalTo(userListCollectionView.snp.bottom).offset(30)
            make.leading.equalTo(customView.snp.leading).offset(30)
            make.trailing.equalTo(customView.snp.trailing).offset(-30)
            make.bottom.equalTo(customView.snp.bottom)
            make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width)
        }
         
        
    }
    
    /*
     etc
     */
    
    // MARK: 뒤로가기 버튼
    @objc
    private func clickedBackBtn(){
        self.dismiss(animated: true)
    }
    
    // MARK: 색상 랜덤 설정
    private func randomColor(){
        for _ in 0..<userListCount{
            let r: CGFloat = CGFloat.random(in: 0...1)
            let g: CGFloat = CGFloat.random(in: 0...1)
            let b: CGFloat = CGFloat.random(in: 0...1)
            dataColor.append([UIColor(red: r, green: g, blue: b, alpha: 0.8)])
        }
    }
    
    // MARK: 테스트 데이터 넣기
    private func testData(){
        for i in 1...10{
            self.userName.append("a")
            self.userCommit.append(i)
        }
        addDetailUI()
        sparkLineView.inputData(lineList: [0,11,23,13,4,52,6,7,8,9])
        setchartOption()
    }
}

extension RepoDetailController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UserListCollectionViewCell.identifier, for: indexPath) as? UserListCollectionViewCell else { return UICollectionViewCell()}
        
        cell.layer.cornerRadius = 15
        cell.backgroundColor = UIColor(red: 252/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
        cell.layer.borderWidth = 1
        cell.layer.borderColor = CGColor(red: 255/255, green: 255/255, blue: 255/255, alpha: 0.5)
        cell.layer.shadowOpacity = 0.5
        cell.layer.shadowOffset = CGSize(width: -3, height: 3)

        cell.inputData(name: self.userName[indexPath.row],
                       commits: self.userCommit[indexPath.row])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return self.userListCount }
        
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = self.view.safeAreaLayoutGuide.layoutFrame.width*4/5
        let height = self.view.safeAreaLayoutGuide.layoutFrame.height/10
        
        return CGSize(width: width, height: height)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print(indexPath.row)
        collectionView.deselectItem(at: indexPath, animated: true)
    }
}

// MARK: Chart 설정
extension RepoDetailController: ChartViewDelegate {
    
    // 차트 생성
    private func setchartOption(){
        
        var dataSet: [BarChartDataSet] = []
        
        for i in 0..<self.userListCount{
            var contributorInfo = [ChartDataEntry]()
            
            let dataEntry = BarChartDataEntry(x: Double(i), y: Double(userCommit[i]))
            contributorInfo.append(dataEntry)
            
            let set1 = BarChartDataSet(entries: contributorInfo, label: userName[i])
            
            set1.colors = dataColor[i]
            set1.valueTextColor = .black
            set1.valueColors = [.black]
            dataSet.append(set1)
        }

        let data = BarChartData(dataSets: dataSet)
        data.setValueFont(UIFont.systemFont(ofSize: 12))    // 그래프 위 숫자
        barChart.data = data
        customChart()
    }
    
    // 차트 옵션 설정
    private func customChart(){
        
        barChart.rightAxis.enabled = false
        barChart.animate(xAxisDuration: 2, yAxisDuration: 2)
        barChart.leftAxis.enabled = true
        barChart.doubleTapToZoomEnabled = false
        barChart.xAxis.enabled = false
        barChart.leftAxis.labelFont = .systemFont(ofSize: 15)
        barChart.legend.textColor = .black
        barChart.noDataText = "출력 데이터가 없습니다."
        barChart.noDataFont = .systemFont(ofSize: 30)
        barChart.noDataTextColor = .lightGray
        
    }
    
}



import SwiftUI
struct VCPreViewRepoDetailController:PreviewProvider {
    static var previews: some View {
        RepoDetailController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewRepoDetailController2:PreviewProvider {
    static var previews: some View {
        RepoDetailController().toPreview().previewDevice("iPhone 11")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
