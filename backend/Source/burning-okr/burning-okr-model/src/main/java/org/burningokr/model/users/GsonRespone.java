package org.burningokr.model.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GsonRespone {

  @SerializedName("@odata.context")
  @Expose
  private String odataContext;

  @SerializedName("@odata.nextLink")
  @Expose
  private String odataNextLink;

  @SerializedName("value")
  @Expose
  private List<User> gsonUser = null;

  public String getOdataContext() {
    return odataContext;
  }

  public void setOdataContext(String odataContext) {
    this.odataContext = odataContext;
  }

  public String getOdataNextLink() {
    return odataNextLink;
  }

  public void setOdataNextLink(String odataNextLink) {
    this.odataNextLink = odataNextLink;
  }

  public List<User> getUsers() {
    return gsonUser;
  }
}
