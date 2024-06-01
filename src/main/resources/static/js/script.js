/**
 * 
 */
console.log("This is working Fine");

const toggleSidebar = () => {
	if($(".sidebar").is(":visible")) {
		//true
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		//false
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
}

const search = () => {
	
	let query = $("#search-input").val();
	
	if(query == "")
	{
		$(".search-result").hide();	
	} else {
		//Sending request to Server
		
		let url = `http://localhost:8080/search/${query}`;
		
		fetch(url).then((response) => {
			
			return response.json();
			})
			.then((data) => {
				
				let text = `<div class='list-group'>`;
				
				data.forEach((contacts) => {
					text+=`<a href='/user/${contacts.cId}/contact' class='list-group-item list-group-action'> ${contacts.name} </a>`;
				});
	
			$(".search-result").html(text);	
			$(".search-result").show();	
				
			});
	}
	
}
