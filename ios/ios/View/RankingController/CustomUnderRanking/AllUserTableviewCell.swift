//
//  AllUserTableviewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

final class AllUserTableviewCell: UITableViewCell{
    static let identifier = "AllUserTableviewCell"
    
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 랭킹 라벨
    private lazy var rankingLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        return label
    }()
    
    // MARK:
    private lazy var customUIView: CustomUserView = {
        let view = CustomUserView()
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1.5
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.shadowOpacity = 0.4
        view.backgroundColor = .white
        return view
    }()
    
    private func addUI_SetAutoLayout(){
        self.contentView.addSubview(rankingLabel)
        self.contentView.addSubview(customUIView)
        
        rankingLabel.snp.makeConstraints { make in
            make.leading.equalTo(30)
            make.centerY.equalToSuperview()
        }
        
        customUIView.snp.makeConstraints { make in
            make.leading.equalTo(rankingLabel.snp.trailing).offset(20)
            make.centerY.equalToSuperview()
            make.top.equalToSuperview().offset(10)
            make.bottom.equalToSuperview().offset(-10)
            make.trailing.equalToSuperview().offset(-30)
        }
        
    }
    
    // MARK: tableView cell 데이터 넣기
    func inputData(rank: Int, userData: AllUserRankingModel){
        addUI_SetAutoLayout()
        rankingLabel.text = "\(rank)"
        customUIView.getData(data: userData)
        
        switch userData.tier {
        case "BRONZE":
            customUIView.layer.borderColor = CGColor(red: 101/255, green: 4/255, blue: 4/255, alpha: 1.0) /* #650404 */
        case "SILVER":
            customUIView.layer.borderColor = CGColor(red: 46/255, green: 198/255, blue: 189/255, alpha: 1.0) /* #2ec6bd */
        case "GOLD":
            customUIView.layer.borderColor = CGColor(red: 245/255, green: 238/255, blue: 176/255, alpha: 1.0) /* #f5eeb0 */
        default:
            customUIView.layer.borderColor = CGColor(red: 255/255, green: 255/255, blue: 255/255, alpha: 1.0)
        }
    }
    
}
