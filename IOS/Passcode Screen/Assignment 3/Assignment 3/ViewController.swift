//
//  ViewController.swift
//  Assignment 3
//
//  Created by Tsotne Babunashvili on 07.12.21.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet var topViews: [UIView]!
    @IBOutlet var buttonViews: [ButtonView]!
    var mainTexts = ["1","2","3","4","5","6","7","8","9","0"]
    var bottomTexts = ["","ABC","DEF","GHI","JKL","MNO","PQRS","TUV","WXYZ", ""]
    var password = ""
    var passDigInd = -1
    @IBOutlet var deleteButton : UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        for buttonView in buttonViews {
            buttonView.delegate = self
        }
    }


    override func viewDidLayoutSubviews() {
        for topView in topViews {
            topView.layer.cornerRadius = topView.frame.width/2
            topView.clipsToBounds = true
            topView.layer.borderWidth = 1
            topView.layer.borderColor = UIColor.white.cgColor
            topView.backgroundColor = .black
        }
        
        for i in 0...9 {
            buttonViews[i].changeMainLabel(mainTexts[i])
            buttonViews[i].changeBottomLabel(bottomTexts[i])
        }
        deleteButton.backgroundColor = .black
    }
    
    @IBAction func deleteTap() {
        if ((passDigInd>=0) && (passDigInd<=3)) {
            topViews[passDigInd].backgroundColor = .black
            let i = password.index(password.startIndex, offsetBy: passDigInd)
            password.remove(at: i)
            passDigInd-=1
        }
    }
}

extension ViewController : ButtonViewDelegate {
    func handleButtonTouchUp(_ sender: ButtonView) {
        sender.button.backgroundColor = .black
        password += sender.mainLabel
        passDigInd += 1
        if (passDigInd <= 3) {
            topViews[passDigInd].backgroundColor = .gray
        }
        if (passDigInd == 3) {
            for topView in topViews {
                topView.backgroundColor = .black
            }
            if (password == "1111") {
                let viewController = storyboard?.instantiateViewController(identifier: "ScreenUnlocked")
                navigationController?.pushViewController(viewController!, animated: true)
            }
            password = ""
            passDigInd = -1
        }
    }
    
    func handleButtonDragExit(_ sender: ButtonView) {
        sender.button.backgroundColor = .black
    }
    
    func handleButtonTouchDown(_ sender: ButtonView) {
        sender.button.backgroundColor = .gray
    }
}

