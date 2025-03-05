package tn.esprit.controllers;

import tn.esprit.entities.User;
import tn.esprit.utils.EmailSender;

public class AdminController {

    // Notify the user about updates to their account
    public void notifyUserOfChanges(User user, String updatedInfo, boolean isDeleted) {
        String subject = isDeleted ? "Your Account Has Been Deactivated" : "Your Account Has Been Updated";
        String message = "Hello " + user.getPrenom() + ",\n\n";

        if (isDeleted) {
            // Account deletion message
            message += "We regret to inform you that your account has been deactivated.\n\n" +
                    "If you believe this is a mistake or have any questions, please contact support.";
        } else {
            // Account update message
            message += "Your account has been updated by the admin.\n\n" +
                    "Updated Information:\n" +
                    updatedInfo + "\n\n" +
                    "If you did not request these changes, please contact support.";
        }

        // Send email using EmailSender
        EmailSender.sendEmail(user.getMail(), subject, message);
    }
}
