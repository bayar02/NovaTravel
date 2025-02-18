package tn.esprit.entities;

import java.sql.Date;

public class reclamation {



        private int id;
        private int id_user;
        private Date date_reclamation;
        private String type;
        private String message;

        public reclamation() {
        }

        public reclamation(  int id , int id_user, Date date_reclamation, String type, String message) {
            this.id = id;
            this.id_user = id_user;
            this.date_reclamation = date_reclamation;
            this.type = type;
            this.message = message;
        }
        public reclamation(int id_user,  Date date_reclamation, String type, String message) {
            this.id_user = id_user;

            this.date_reclamation = date_reclamation;
            this.type = type;
            this.message = message;
        }

    public reclamation(  Date date_reclamation, String type, String message) {

        this.date_reclamation = date_reclamation;
        this.type = type;
        this.message = message;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId_user() {
            return id_user;
        }

        public void setId_user(int id_user) {
            this.id_user = id_user;
        }

        public Date getDate_reclamation() {
            return date_reclamation;
        }

        public void setDate_reclamation(Date date_reclamation) {
            this.date_reclamation = date_reclamation;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Reclamation {" +
                    "id=" + id +
                    ", id_user=" + id_user +
                    ", date_reclamation='" + date_reclamation + '\'' +
                    ", type='" + type + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }


