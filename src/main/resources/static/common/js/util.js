
class Util {
    static formDataToJson(formId) {
        const formElement = document.querySelector("#" + formId),
            inputElements = formElement.getElementsByTagName("input"),
            jsonObject = {};

        for (var i = 0; i < inputElements.length; i++) {
            var inputElement = inputElements[i];
            jsonObject[inputElement.name] = inputElement.value;

        }

        return JSON.stringify(jsonObject);
    }
}

class Error{
    static handleFetchError(response){
        if(!response.ok) {
            throw response;
        }
        return response;
    }
}