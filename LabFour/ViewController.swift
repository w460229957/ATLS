//
//  ViewController.swift
//  LabFour
//
//  Created by 张正源 on 9/29/21.
//

import UIKit

class ViewController: UIViewController,UITextFieldDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        enteredNumber.delegate = self
        NotificationCenter.default.addObserver(self, selector:#selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        
        NotificationCenter.default.addObserver(self, selector:#selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
         view.addGestureRecognizer(tap)
        

        
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
        updateWeight()
     }
    
    @objc func keyboardWillShow(notification: NSNotification) {
     if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
     if self.view.frame.origin.y == 0 {
                                        self.view.frame.origin.y -= keyboardSize.height
                                            }
                                }
                }
    
    @objc func keyboardWillHide(notification: NSNotification) {
     if ((notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue) != nil {
                if self.view.frame.origin.y != 0 {
                                    self.view.frame.origin.y = 0
                    }
            }
     }
    
    @IBOutlet weak var enteredNumber: UITextField!
    @IBOutlet weak var Result: UILabel!
    @IBOutlet weak var StepperValue: UIStepper!
    
    
    func updateWeight(){
        if(!enteredNumber.text!.isEmpty){
        let result = Double(enteredNumber.text!)! * StepperValue.value
        Result.text = "Weight: \(result) = C: \(StepperValue.value) * \(enteredNumber.text!)"
        }
        else{
            let alert = UIAlertController(title: "Invalid Input", message: "You typed a wrong input", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK",style:.destructive,handler:{_ in
                self.Result.text = "Weight : 12 = C: 9.8 * 3.66"
                self.StepperValue.value = 0
                self.enteredNumber.text = ""
            }))
        }
    }
    @IBAction func multiplier(_ sender: UIStepper!) {
        if StepperValue.value >= 0 {
            updateWeight()
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            updateWeight()
        textField.resignFirstResponder()
        return true
    }
    
}

