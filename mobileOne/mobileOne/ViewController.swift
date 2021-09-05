//
//  ViewController.swift
//  mobileOne
//
//  Created by 张正源 on 9/2/21.
//

import UIKit

class ViewController: UIViewController {
    @IBOutlet weak var imagContent: UIImageView!
    
    
    @IBOutlet weak var text: UILabel!
    @IBAction func buttonOne(_ sender: UIButton) {
        if(sender.tag == 0){
            imagContent.image = UIImage(named: "catgirlOne")
            text.text = "Greetings!"
        }
        else{
            imagContent.image = UIImage(named: "catgirlTwo")
            text.text = "GoodBye!"
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


}

