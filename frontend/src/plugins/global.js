import Vue from "vue";

Vue.prototype.timeFormat = function (postTime) {
  var nowTime = new Date() / 1000;
  var diffTime = nowTime - postTime;
  var seconds = parseInt(diffTime);
  var minutes = parseInt(diffTime / 60);
  var hours = parseInt(diffTime / 3600);
  var days = parseInt(diffTime / (3600 * 24));
  if (days <= 30)
    if (days <= 0)
      if (hours <= 0)
        if (minutes <= 0) return seconds + "秒前";
        else return minutes + "分钟前";
      else return hours + "小时前";
    else return days + "天前";
  else {
    var date = new Date(postTime * 1000);
    var y = date.getFullYear();
    var m = date.getMonth();
    var d = date.getDay();
    return y + "年" + m + "月" + d + "天";
  }
};

Vue.prototype.vote = function (id, value, type) {
  this.$axios({
    method: "post",
    url: "/api/" + type,
    data: {
      id: id,
      value: value,
    },
  })
    .then((response) => {
      if (response.data.success === true) {
        return true;
      } else {
        console.log(response.data);
      }
    })
    .catch((error) => {
      console.log(error);
    });
};
