!function(i){i.fn.extend({accordion:function(n){function t(){$this.find("a").click(function(){function n(){return OpenNodeId="",$this.find("li strong").each(function(){0!=i(this).parent().find("ul:first").is(":visible")&&(""==OpenNodeId?OpenNodeId=i(this).parent().attr("id"):OpenNodeId+=","+i(this).parent().attr("id"))}),OpenNodeId}function t(n){var t=!1,s=n.split("/"),e=s[s.length-1],r=e.split("."),a=r[r.length-2];return $this.find("li").each(function(){return t=i(this).attr("id")==a,!t}),t}var s=i(this).attr("href");1==t(s)&&(s+="?"+n(),i(this).attr("href",s))})}function s(){var n=window.location.href,t=n.split("/"),s=t[t.length-1],e=s.split("."),r=e[e.length-2];$this.find("li").each(function(){i(this).attr("id")==r&&(0==i(this).parent().parent().find("ul:first").is(":visible")&&i(this).parent().parent().find("ul:first").show(),i(this).find("a").css("color","#33a3dc"))})}var e={accordion:"true",speed:300,closedSign:'&nbsp;&nbsp;<i class="fa fa-chevron-circle-right"></i>',openedSign:'&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i>'},r=i.extend(e,n);$this=i(this),$this.find("li").each(function(){0!=i(this).find("ul").size()&&(i(this).find("strong:first").append("<span>"+r.closedSign+"</span>"),"sidebar-title"==i(this).find("strong:first").attr("class")&&i(this).find("strong:first").click(function(){return!1}))}),$this.find("li.active").each(function(){i(this).parents("ul").slideDown(r.speed),i(this).parents("ul").parent("li").find("span:first").html(r.openedSign)}),$this.find("li strong").click(function(){0!=i(this).parent().find("ul").size()&&(r.accordion&&(i(this).parent().find("ul").is(":visible")||(parents=i(this).parent().parents("ul"),visible=$this.find("ul:visible"),visible.each(function(n){var t=!0;parents.each(function(i){if(parents[i]==visible[n])return t=!1,!1}),t&&i(this).parent().find("ul")!=visible[n]&&i(visible[n]).slideUp(r.speed,function(){i(this).parent("li").find("span:first").html(r.closedSign)})}))),i(this).parent().find("ul:first").is(":visible")?i(this).parent().find("ul:first").slideUp(r.speed,function(){i(this).parent("li").find("span:first").delay(r.speed).html(r.closedSign)}):i(this).parent().find("ul:first").slideDown(r.speed,function(){i(this).parent("li").find("span:first").delay(r.speed).html(r.openedSign)}))});for(var a=window.location.search.substr(1),d=a.split(","),f=0;f<d.length;f++){var l=document.getElementById(d[f]),c=i(l);c.find("ul:first").show(),c.find("span:first").html(r.openedSign)}t(),s()},expand:function(){function n(){$this.find("a").click(function(){function n(n){var t=!1,s=n.split("/"),e=s[s.length-1],r=e.split("."),a=r[r.length-2];return $this.find("li").each(function(){return t=i(this).attr("id")==a,!t}),t}var t=i(this).attr("href");1==n(t)&&(t+="?"+OpenNode(),i(this).attr("href",t))})}n()}})}(jQuery),$(document).ready(function(){$(".topnav").accordion({accordion:!1,speed:500,closedSign:'&nbsp;&nbsp;<i class="fa fa-chevron-circle-right"></i>',openedSign:'&nbsp;&nbsp;<i class="fa fa-chevron-circle-down"></i>'})});