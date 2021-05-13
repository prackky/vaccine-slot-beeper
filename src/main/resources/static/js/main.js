$(document).ready(function() {
	
	let params = (new URL(document.location)).searchParams;
	if(params.get("text")){
		let text = params.get("text");
		let itemText = $('#status');
		itemText.text(text);
	}
	
	
	/*\
	 * ================================================================== [
	 * Validate ]
	 */
	var input = $('.validate-input .input100');

	$('#subscribe').on('submit', function(e) {
		e.preventDefault(); // avoid to execute the actual submit of the form.
		var check = true;
		
		for (var i = 0; i < input.length; i++) {
			if (validate(input[i]) == false) {
				showValidate(input[i]);
				check = false;
			}
		}
		console.log('Checked : ',check);
		var form = $(this);
		var url = form.attr('action');
		if(check){
			var reg = new Object();
			reg.pinCode = $('#pinCode').val();
			console.log(reg);
			$('#subscribe').html("Please wait...")
			$.ajax({
				url: url,
				type: 'POST',
				dataType: 'json',
				data: JSON.stringify(reg),
				contentType: 'application/json', // serializes the form's elements.
				statusCode: {
	                200: function(response) {
	                    console.log('ajax.statusCode: 200');
		                location.href="../status?text="+ response.message;
	                },
	                400: function(response) {
	                    console.log('ajax.statusCode: 400');
	                    alert("[ ERROR ] Please correct your email or pincode.");
	                    window.location.reload();
	                },
	                409: function(response) {
	                    console.log('ajax.statusCode: 409');
	                },
	                500: function(response) {
	                    console.log('ajax.statusCode: 500');
	                    alert("We are unable to handle your request right now, try again.");
	                    window.location.reload();
	                }
	            }
			});
		} else {
			console.log('Issue with request');
		}
		$('.validate-form').each(function(){
		    this.reset();
		});
		return check;
	});

	$('.validate-form .input100').each(function() {
		$(this).focus(function() {
			hideValidate(this);
		});
	});

	function validate(input) {
		if ($(input).attr('type') == 'email'
				|| $(input).attr('name') == 'email') {
			if ($(input)
					.val()
					.trim()
					.match(
							/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
				return false;
			}
		} else {
			if ($(input).val().trim() == '') {
				return false;
			}
		}
	}

	function showValidate(input) {
		var thisAlert = $(input).parent();

		$(thisAlert).addClass('alert-validate');
	}

	function hideValidate(input) {
		var thisAlert = $(input).parent();

		$(thisAlert).removeClass('alert-validate');
	}

});