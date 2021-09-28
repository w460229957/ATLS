//
//  ViewController.swift
//  Project
//
//  Created by 张正源 on 9/27/21.
//

import UIKit

class ViewController: UIViewController,UITextFieldDelegate{

    @IBOutlet weak var answer: UITextField!
    @IBOutlet weak var testChoice: UISegmentedControl!
    @IBOutlet weak var timeRemained: UILabel!
    @IBOutlet weak var time: UILabel!
    @IBOutlet weak var ConcenQuestion: UILabel!
    @IBOutlet weak var shortMemQuestion: UIImageView!
    @IBOutlet weak var questionTitle: UILabel!
    @IBOutlet weak var bigTitle: UILabel!
    @IBOutlet weak var shortImage: UIImageView!
    @IBOutlet weak var answerField: UILabel!
    @IBOutlet weak var startButton: UIButton!
    @IBOutlet weak var resetButton: UIButton!
    @IBOutlet weak var checkButton: UIButton!
    
    var choice = false
    override func viewDidLoad() {
        answer.delegate = self
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        if !startButton.isEnabled {
            if Float(textField.text!) == 181265{
                ConcenQuestion.text = "True"
                ConcenQuestion.font = UIFont.systemFont(ofSize: 20.0)
                ConcenQuestion.textColor = UIColor.systemBlue;
            }
            else{
                ConcenQuestion.text = "Wrong"
                ConcenQuestion.font = UIFont.systemFont(ofSize: 20.0)
                ConcenQuestion.textColor = UIColor.systemRed
            }
        }
        return true
    }
    func concentrationTestIni(){
        shortMemQuestion.isHidden = true
        ConcenQuestion.isHidden = false
        shortImage.image = nil
        checkButton.isHidden = true
        //Implement timer function
        questionTitle.text = "Sample:type backwards the number:"
        ConcenQuestion.text = "ClickStart to Begin"
        questionTitle.isHidden = false
        bigTitle.text = "Concentration Test"
        answerField.isHidden = false
        answer.isHidden = false
        choice = true
        answerField.text = "type your answer"
    }
    @IBAction func startTheTest(_ sender: UIButton!) {
        if testChoice.selectedSegmentIndex != -1{
        time.isHidden = false
        timeRemained.isHidden = false
        sender.isEnabled = false
        testChoice.isEnabled = false
        ConcenQuestion.text = "562181"
        }
    }
    @IBAction func resetTheTest(_ sender: UIButton!) {
        shortMemQuestion.isHidden = true
        ConcenQuestion.isHidden = true
        shortImage.image = nil
        ConcenQuestion.text = ""
        questionTitle.text = ""
        questionTitle.isHidden = true
        bigTitle.text = "My Test APP"
        answerField.isHidden = true
        answer.isHidden = true
        time.isHidden = true
        timeRemained.isHidden = true
        startButton.isEnabled = true
        testChoice.isEnabled = true
        testChoice.selectedSegmentIndex = -1
        checkButton.isHidden = true
    }
    
    func shortMemoryTestIni(){
        answerField.isHidden = false
        shortMemQuestion.isHidden = false
        ConcenQuestion.isHidden = true
        ConcenQuestion.text = ""
        questionTitle.text = "Sample:Remember each picture in sequence"
        questionTitle.isHidden = false
        bigTitle.text = "Short Memory Test"
        answer.isHidden = true
        answerField.isHidden = true
        choice = true
        shortMemQuestion.image = UIImage(named: "catGirl")
        checkButton.isHidden = false
        
    }
    
    @IBAction func UserHasChosenTest(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 1 {
            answer.resignFirstResponder()
            concentrationTestIni()
        }
        else
        {
            answer.resignFirstResponder()
            shortMemoryTestIni()
        }
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.placeholder = "Enter your code"
        ConcenQuestion.textColor = UIColor.black
    }
    var changed = false
    var count:Int = 0
    @IBAction func changePicture(_ sender: UIButton!) {
        if(!changed){
            bigTitle.text = "picture1"
            shortMemQuestion.image = UIImage(named: "catGirlTwo")
            changed = true
            count = count + 1
        }
        else{
            bigTitle.text = "picture2"
            shortMemQuestion.image = UIImage(named: "catGirl")
            changed = false
            count = count  + 1
        }
        
        if(count == 3){
            bigTitle.text = "picture2"
            shortImage.image = UIImage(named: "catGirl")
            checkButton.isHidden = true
            answer.isHidden = false
            answerField.isHidden = false
            answerField.text = "type the second picture in the sequence"
        }
    }
    
}

