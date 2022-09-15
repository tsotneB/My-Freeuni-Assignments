//
//  ButtonView.swift
//  Assignment 3
//
//  Created by Tsotne Babunashvili on 07.12.21.
//

import UIKit

protocol ButtonViewDelegate : AnyObject {
    func handleButtonTouchUp(_ sender: ButtonView)
    func handleButtonDragExit(_ sender: ButtonView)
    func handleButtonTouchDown(_ sender: ButtonView)
}

class ButtonView: UIView {
    @IBOutlet var contentView: UIView!
    @IBOutlet var button: UIButton!
    @IBOutlet var numLabel: UILabel!
    @IBOutlet var charLabel: UILabel!
    var mainLabel : String!
    var bottomLabel : String!
    
    weak var delegate: ButtonViewDelegate?
    override init(frame:  CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        commonInit()
    }
    
    func changeMainLabel (_ digit : String) {
        mainLabel = digit
        numLabel.text = mainLabel
    }
    
    func changeBottomLabel (_ characters : String) {
        bottomLabel = characters
        charLabel.text = bottomLabel
    }
    
    func commonInit() {
        let bundle = Bundle(for: ButtonView.self)
        bundle.loadNibNamed("ButtonView", owner: self, options: nil)
        
        contentView.frame = bounds
        contentView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        
        button.backgroundColor = .black
        
        button.layer.borderWidth = 1
        button.layer.borderColor = UIColor.white.cgColor
        
        addSubview(contentView)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        button.layer.cornerRadius = button.frame.width/2
        button.clipsToBounds = true
    }
    
    @IBAction func handleButtonTap() {
        delegate?.handleButtonTouchUp(self)
    }
    
    @IBAction func handleButtonDragExit() {
        delegate?.handleButtonDragExit(self)
    }
    
    @IBAction func handleButtonTouch() {
        delegate?.handleButtonTouchDown(self)
    }
}
