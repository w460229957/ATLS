//
//  ViewController.swift
//  Project
//
//  Created by 张正源 on 9/27/21.
//

import UIKit
import Foundation

class ViewController: UIViewController{
            
    @IBOutlet weak var homeScreen: UIImageView!
    @IBOutlet weak var number: UILabel!
    
    override func viewDidLoad() {
        // Do any additional setup after loading the view.
        super.viewDidLoad()
        homeScreen.layer.borderWidth = 1
        homeScreen.layer.masksToBounds = true
        homeScreen.layer.borderColor = UIColor.blue.cgColor
        homeScreen.layer.cornerRadius = CGFloat(18)
        number.layer.borderColor = UIColor.blue.cgColor
        number.layer.borderWidth = 1
        number.layer.cornerRadius = CGFloat(18)
        number.layer.masksToBounds = true
        autoUpdate()
    }
    
    func autoUpdate(){
        let result = ShortTest.generateRandomImages(20)
        homeScreen.animationImages = result
        homeScreen.animationDuration = 15.0
        homeScreen.startAnimating()
        let randomNumber = ConcentrationTest.generateRandomNumber(10)
        var currentIndex = 0
        Timer.scheduledTimer(withTimeInterval: 3.0, repeats: true, block: {
            timer in
            if(currentIndex >= 9){
                currentIndex = 0
            }
            else{
                currentIndex += 1
            }
            self.number.text = String(randomNumber[currentIndex])
        })
        
    }
    

    
}

