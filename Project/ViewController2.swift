//
//  ViewController2.swift
//  Project
//
//  Created by 张正源 on 10/5/21.
//

import UIKit

class ViewController2: UIViewController,UITextFieldDelegate{
    var totalScore = 0
    var timer:Timer?
    @IBOutlet weak var button: UIButton!
    
    @IBOutlet weak var answerfield: UITextField!
    
    @IBOutlet weak var textTitle: UIStackView!
    
    @IBOutlet weak var numberField: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        answerfield.delegate = self
        // Do any additional setup after loading the view.
        
        ////This part of code is from the textbook////////////////////////////////////////////////
        NotificationCenter.default.addObserver(self, selector:#selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        
        NotificationCenter.default.addObserver(self, selector:#selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
         view.addGestureRecognizer(tap)
        
    }

    @objc func dismissKeyboard() {
          view.endEditing(true)
       }
      
      @objc func keyboardWillShow(notification: NSNotification) {
       if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
       if self.view.frame.origin.y == 0 {
                                          self.view.frame.origin.y -= keyboardSize.height-150
                                              }//I added -150 to adjust the y coordinate of the frame
                                  }
                  }
      
      @objc func keyboardWillHide(notification: NSNotification) {
       if ((notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue) != nil {
                  if self.view.frame.origin.y != 0 {
                                      self.view.frame.origin.y = 0
                      }
              }
       }
/////////////////////////////////////////////////////////////////////////////////////
    func removeTextTitle(){
        textTitle.isHidden = true
    }
    
    func showTextTitle(){
        textTitle.isHidden = false
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    

    func showNumberField(){
        numberField.isHidden = false
    }
    
    func removeNumberField(){
        numberField.isHidden = true
    }
    
    func setNumberField(_ input:Int){
        numberField.text = "Number: "+String(input)
    }
    
    func showTextField(){
        answerfield.isHidden = false
    }
    
    func hideTextField(){
        answerfield.isHidden = true
    }
    
    func getTextField()-> String{
        return answerfield.text!
    }
    func buttonStart(){
        button.setTitle("Start", for: .normal)
    }
    
    func buttonStop(){
        button.setTitle("Stop", for: .normal)
    }
    
    func CheckIfRight(CorrectAnswer str1:String,UserAnswer str2:String)->Bool{
        if(str1 == str2){
            return true
        }
        else{
            return false
        }
    }
    
    func IncrementScore(){
        totalScore += 1
    }
    func CalculateResultANDShow(_ result:Int){
        timer?.invalidate()
        showTextTitle()
        hideTextField()
        answerfield.text! = ""
        showNumberField()
        numberField.text = "Your final score is \(round((Double(result)/30)*100))"
        
        
    }
    
    func startTest(){
        numberField.text = "wait....."
        removeTextTitle()
        showNumberField()
        var testset = ConcentrationTest.generateRandomNumber(30)
        var answerset = ConcentrationTest.revertNumber(testset)
        var time = 0
        var flag:Bool = true
        timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true, block: {
            timer in
            time -= 1
            if(time <= 0){
                        if(flag == false){
                            flag = true
                            time = 10
                            self.removeNumberField()
                            self.showTextField()
                        }
                        else{
                            flag = false
                            time = 6
                            if(testset.count == 0){
                                self.CalculateResultANDShow(self.totalScore)
                            }
                            else{
                                    if(self.CheckIfRight(CorrectAnswer: String(answerset.last!), UserAnswer: self.getTextField())){
                                        self.IncrementScore()
                                    }
                                    self.setNumberField(testset.last!)
                                    testset = testset.dropLast()
                                    answerset = answerset.dropLast()
                                    self.showNumberField()
                                    self.hideTextField()
                                    self.answerfield.text = ""
                            }

                    }
                }

            }
        )
        
    }
    
    @IBAction func startOrStop(_ sender: UIButton) {
        if(sender.currentTitle == "Start!"){
            sender.setTitle("Stop!", for: .normal)
            self.startTest()
        }
        else{
            sender.setTitle("Start!", for: .normal)
            self.CalculateResultANDShow(self.totalScore)
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
