/**
* Handler that will be called during the execution of a PostLogin flow.
*
* @param {Event} event - Details about the user and the context in which they are logging in.
* @param {PostLoginAPI} api - Interface whose methods can be used to change the behavior of the login.
*/

exports.onExecutePostLogin = async (event, api) => {
  let uuidv4 = require('uuid').v4;
  let CryptoJS = require('crypto-js');
  const sha512 = require('crypto').createHash('sha512');
  let user = event.user;
  if(user.user_metadata.isResetPasswordOnFirstLogin && user.user_metadata.isResetPasswordOnFirstLogin === true) {
    let uuid = event.user.user_id;
    let temp_password_hash = uuidv4();
    let wordArray = CryptoJS.enc.Utf8.parse(uuid + ":" + temp_password_hash);
    let passwordResetHash = CryptoJS.enc.Base64.stringify(wordArray);
    let hashedPassword = sha512.update(temp_password_hash, "utf-8").digest().toString("base64url");
    api.user.setUserMetadata("passwordResetHash",hashedPassword);
    api.user.setUserMetadata("passwordResetHashCreated", Math.floor((new Date()).getTime()));
    api.redirect.sendUserTo("http://localhost:3000/reset-password/" + passwordResetHash);
  }
  
};

exports.onContinuePostLogin = async (event, api) => {
}

/**
* Handler that will be invoked when this action is resuming after an external redirect. If your
* onExecutePostLogin function does not perform a redirect, this function can be safely ignored.
*
* @param {Event} event - Details about the user and the context in which they are logging in.
* @param {PostLoginAPI} api - Interface whose methods can be used to change the behavior of the login.
*/
// exports.onContinuePostLogin = async (event, api) => {
// };
