//
//  ForecastCell.swift
//  Weather
//
//  Created by Tsotne Babunashvili on 21.02.22.
//

import UIKit

class ForecastCell: UITableViewCell {
    @IBOutlet var hourLabel : UILabel!
    @IBOutlet var weatherLabel : UILabel!
    @IBOutlet var tempLabel: UILabel!
    @IBOutlet var weatherImage : UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
