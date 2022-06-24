package com.aecb.data.api.models.adcb;

import com.google.gson.annotations.SerializedName;

public class MakePayment6Data{

	@SerializedName("result")
	private String result;

	@SerializedName("timeOfRecord")
	private String timeOfRecord;

	@SerializedName("gatewayEntryPoint")
	private String gatewayEntryPoint;

	@SerializedName("response")
	private Response response;

	@SerializedName("3DSecureId")
	private String jsonMember3DSecureId;

	@SerializedName("authorizationResponse")
	private AuthorizationResponse authorizationResponse;

	@SerializedName("merchant")
	private String merchant;

	@SerializedName("sourceOfFunds")
	private SourceOfFunds sourceOfFunds;

	@SerializedName("3DSecure")
	private JsonMember3DSecure jsonMember3DSecure;

	@SerializedName("version")
	private String version;

	@SerializedName("transaction")
	private Transaction transaction;

	@SerializedName("order")
	private Order order;

	@SerializedName("paymentRef")
	private String paymentRef;

	@SerializedName("resultCode")
	private String resultCode;

	@SerializedName("resultMessage")
	private String resultMessage;

	@SerializedName("paymentdate")
	private String paymentdate;

	@SerializedName("transactionID")
	private String transactionID;

	@SerializedName("paymentSource")
	private String paymentSource;

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getPaymentSource() {
		return paymentSource;
	}

	public void setPaymentSource(String paymentSource) {
		this.paymentSource = paymentSource;
	}

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setTimeOfRecord(String timeOfRecord){
		this.timeOfRecord = timeOfRecord;
	}

	public String getTimeOfRecord(){
		return timeOfRecord;
	}

	public void setGatewayEntryPoint(String gatewayEntryPoint){
		this.gatewayEntryPoint = gatewayEntryPoint;
	}

	public String getGatewayEntryPoint(){
		return gatewayEntryPoint;
	}

	public void setResponse(Response response){
		this.response = response;
	}

	public Response getResponse(){
		return response;
	}

	public void setJsonMember3DSecureId(String jsonMember3DSecureId){
		this.jsonMember3DSecureId = jsonMember3DSecureId;
	}

	public String getJsonMember3DSecureId(){
		return jsonMember3DSecureId;
	}

	public void setAuthorizationResponse(AuthorizationResponse authorizationResponse){
		this.authorizationResponse = authorizationResponse;
	}

	public AuthorizationResponse getAuthorizationResponse(){
		return authorizationResponse;
	}

	public void setMerchant(String merchant){
		this.merchant = merchant;
	}

	public String getMerchant(){
		return merchant;
	}

	public void setSourceOfFunds(SourceOfFunds sourceOfFunds){
		this.sourceOfFunds = sourceOfFunds;
	}

	public SourceOfFunds getSourceOfFunds(){
		return sourceOfFunds;
	}

	public void setJsonMember3DSecure(JsonMember3DSecure jsonMember3DSecure){
		this.jsonMember3DSecure = jsonMember3DSecure;
	}

	public JsonMember3DSecure getJsonMember3DSecure(){
		return jsonMember3DSecure;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
		return version;
	}

	public void setTransaction(Transaction transaction){
		this.transaction = transaction;
	}

	public Transaction getTransaction(){
		return transaction;
	}

	public void setOrder(Order order){
		this.order = order;
	}

	public Order getOrder(){
		return order;
	}
}