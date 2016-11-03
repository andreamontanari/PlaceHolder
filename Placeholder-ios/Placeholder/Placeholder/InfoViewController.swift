//
//  InfoViewController.swift
//  Placeholder
//
//  Created by Andrea Montanari on 17/08/16.
//  Copyright © 2016 Andrea Montanari. All rights reserved.
//

import Foundation
import UIKit
import MessageUI
import RealmSwift

class InfoViewController: UIViewController, MFMailComposeViewControllerDelegate {
    
    @IBOutlet weak var backBtn: UIBarButtonItem!

    @IBOutlet weak var webBtn: UIButton!
    @IBOutlet weak var fbBtn: UIButton!
    @IBOutlet weak var contactBtn: UIButton!
    @IBOutlet weak var resetBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func goBack(sender: UIBarButtonItem) {
        self.navigationController?.popViewControllerAnimated(true)
        //dismissViewControllerAnimated(true, completion: nil)
    }
    
    
    @IBAction func fbButtonPressed(sender: UIButton) {
        let fbId = "1402856883316764"
        let url = "fb://profile/\(fbId)"
        let fbURL = NSURL(string: url)
        if UIApplication.sharedApplication().canOpenURL(fbURL!){
            UIApplication.sharedApplication().openURL(fbURL!)
        }
        else {
            //redirect to safari because the user doesn't have Facebook
            UIApplication.sharedApplication().openURL(NSURL(string:"http://facebook.com/\(fbId)")!)
        }
    }
    
    
    @IBAction func webButtonPressed(sender: UIButton) {
        
        let url = NSURL(string: "https://github.com/andreamontanari/PlaceHolder/blob/master/README.md")!
        UIApplication.sharedApplication().openURL(url)
    }

    @IBAction func contactPressed(sender: UIButton) {
        
        // write an email to developer
        let mailComposeViewController = configuredMailComposeViewController()
        if MFMailComposeViewController.canSendMail() {
            self.presentViewController(mailComposeViewController, animated: true, completion: nil)
        } else {
            self.showSendMailErrorAlert()
        }

    }
    
    @IBAction func resetPressed(sender: UIButton) {
        
        // dialog: do you want to reset?
        //open dialog
        let alert = UIAlertController(title: NSLocalizedString("RESET_ALL_TITLE", comment: ""), message: NSLocalizedString("RESET_ALL_MSG", comment: ""), preferredStyle: UIAlertControllerStyle.Alert)
        
        alert.addAction(UIAlertAction(title: NSLocalizedString("CANCEL", comment: ""), style: UIAlertActionStyle.Cancel, handler:
            { (action: UIAlertAction!) in
                alert.dismissViewControllerAnimated(true, completion: nil)
        }))
        
        alert.addAction(UIAlertAction(title: NSLocalizedString("RESET_ALL_CONFIRM", comment: ""), style: UIAlertActionStyle.Default, handler:{ (UIAlertAction)in
            // Get the default Realm
            // You only need to do this once (per thread)
            let realm = try! Realm()
            // Add to the Realm inside a transaction
            try! realm.write {
                realm.deleteAll()
            }

        }))
        
        presentViewController(alert, animated: true, completion: nil)
    
    }
    
    func configuredMailComposeViewController() -> MFMailComposeViewController {
        let mailComposerVC = MFMailComposeViewController()
        mailComposerVC.mailComposeDelegate = self // Extremely important to set the --mailComposeDelegate-- property, NOT the --delegate-- property
            
        mailComposerVC.setToRecipients(["dev@mountinnovation.com"])
        mailComposerVC.setSubject(NSLocalizedString("MAIL_OBJECT", comment: ""))
        mailComposerVC.setMessageBody(NSLocalizedString("MAIL_BODY", comment: ""), isHTML: false)
            
        return mailComposerVC
    }
        
    func showSendMailErrorAlert() {

         let sendMailErrorAlert = UIAlertController(title: NSLocalizedString("ERR_MAIL_TITLE", comment: ""), message: NSLocalizedString("ERR_MAIL_MSG", comment: ""), preferredStyle: UIAlertControllerStyle.Alert)
        sendMailErrorAlert.addAction(UIAlertAction(title: NSLocalizedString("CANCEL", comment: ""), style: UIAlertActionStyle.Cancel, handler:
            { (action: UIAlertAction!) in
                sendMailErrorAlert.dismissViewControllerAnimated(true, completion: nil)
        }))

        presentViewController(sendMailErrorAlert, animated: true, completion: nil)
        
        }
        
    // MARK: MFMailComposeViewControllerDelegate
        
    func mailComposeController(controller: MFMailComposeViewController, didFinishWithResult result: MFMailComposeResult, error: NSError?) {
        controller.dismissViewControllerAnimated(true, completion: nil)
            
    }

}
