//
//  ViewController.swift
//  Lab3
//
//  Created by 张正源 on 9/22/21.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var slid: UISlider!
    @IBOutlet weak var capital: UISwitch!
    @IBOutlet weak var segmen: UISegmentedControl!
    @IBOutlet weak var c_picture: UILabel!
    @IBOutlet weak var c_zhang: UILabel!
    @IBOutlet weak var cart_girl: UILabel!
    @IBOutlet weak var cap_name: UILabel!
    

    
    @IBAction func changePic(_ sender:UISegmentedControl!) {
        if(segmen.selectedSegmentIndex == 0){
            image.image = UIImage(named: "catGirlOne")
        }
        else{
            image.image = UIImage(named: "catGirlTwo")
        }
    }
    func changeFont_Color(_ input:UILabel?,_ condExpr:Int){
            if(condExpr == 0){
                input?.text = input?.text?.lowercased();
                input!.textColor = UIColor.systemYellow;
            }
            else{
                input?.text = input?.text?.uppercased();
                input!.textColor = UIColor.systemBlue;
            }
    }
    @IBAction func changeFont(_ sender: UISwitch!) {
        if(sender.isOn){
            changeFont_Color(c_picture,1);
            changeFont_Color(c_zhang,1);
            changeFont_Color(cart_girl,1);
            changeFont_Color(cap_name,1);
        }
        else{
            changeFont_Color(c_picture,0);
            changeFont_Color(c_zhang,0);
            changeFont_Color(cart_girl,0);
            changeFont_Color(cap_name,0);
        }
    }
    
}

