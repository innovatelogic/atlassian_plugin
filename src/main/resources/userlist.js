var followingsList = [];
var USER = "my_user";

SC.get("/users/"+USER+"/followings",{limit:10}, function(users){
    for(var i = 0; i < users.length; i++){
        followingsList.push(users[i].username);
    }
});    