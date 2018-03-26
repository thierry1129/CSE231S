package fibonacci.core;

import java.util.*;

public class test3 {

    class User {
        private String name;
        private int id;

        public User(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.getName() + " " + this.getId();
        }
    }

    class UserService {
        private String svcName;
        //List<User> userList;
        private Map<Integer, String> userMap;
        private List<UserService> listenerList;

        public UserService(String serviceName) {
            if (serviceName == null) {
                // throw an error because userservice name cannot be null,
                //just add a print stmt for now
                System.out.println("user service name cannot be null");
            } else {
                this.svcName = serviceName;

                //this.userList = new ArrayList<>(); // since user id should be unique, it would be easier to use maps, but the solution code
                this.userMap = new HashMap<>();
                this.listenerList = new ArrayList<>();
            }
        }

        public String getSvcName() {
            return this.svcName;
        }
        public Map<Integer,String > getUserMap (){
            return this.userMap;
        }
        public void setUserMap(Map<Integer,String > newMap){
            this.userMap = newMap;
        }
        public List<UserService> getListnerList(){
            return this.listenerList;
        }
        public void setListenerList(List<UserService> newListenerList){
            this.listenerList = newListenerList;
        }

        public void addUser(User user) {
           Map<Integer,String> mapCp = this.getUserMap();
            mapCp.put(user.getId(), user.getName());
            this.setUserMap(mapCp);

            for (UserService listeners : this.getListnerList()) {

                Map<Integer,String> listnerMapCp = listeners.getUserMap();
                listnerMapCp.put(user.getId(), user.getName());
                listeners.setUserMap(listnerMapCp);
            }
        }

        public void deleteUser(User user) {
            deleteHelper(user, this);
            for (UserService listeners : this.getListnerList()) {
                listeners.deleteHelper(user, listeners);
            }
        }

        public void deleteHelper(User user, UserService userService) {
            int id = user.getId();
            Map<Integer,String> userMapCp = userService.getUserMap();
            if (userMapCp.containsKey(id)) {
                userMapCp.remove(id);
                userService.setUserMap(userMapCp);
            } else {
                System.out.println("no user found in map so cannot delete ");
            }
        }

        public List<User> getUsers() {
            List<User> resultList = new ArrayList<>();
            for (Map.Entry<Integer, String> userEntry : this.getUserMap().entrySet()) {
                resultList.add(new User(userEntry.getValue(), userEntry.getKey()));
            }
            return resultList;
        }

        public void registerListener(UserService userService) {
            List<UserService> listenerCp = this.getListnerList();
            listenerCp.add(userService);
            this.setListenerList(listenerCp);
        }

        public void deregisterListener(UserService userService) {
            List<UserService> listenerCp = this.getListnerList();
            listenerCp.remove(userService);
            this.setListenerList(listenerCp);
        }

        public String toString() {
            return this.getSvcName();
        }
    }


    public static void main(String[] args) {

    }
}
