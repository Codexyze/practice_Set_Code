const express = require('express');
const app = express();
app.use(express.json());
app.set('view engine','ejs');
app.use(express.urlencoded(
    {
        extended:true
    }
));

app.get("/",(res,req)=>{
    if(res == null){
        console.log("Error")
    }else{
        //req.send("Data sent here ...")
        req.render("index")
    }
});
app.get("/profile",(res,req)=>{
    if(res == null){
        console.log("Error")
    }else{
        req.send("Profile Data sent here ...")
    }
});
app.listen(3000,()=>{
    console.log("Server stated with port 3000");
});