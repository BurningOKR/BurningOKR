package org.burningokr.service.userutil;

import org.burningokr.service.exceptions.AzureUserFetchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AzurePhotoFetcherTest {

  @Mock
  private AzureApiCaller azureApiCaller;

  @InjectMocks
  private AzurePhotoFetcher azurePhotoFetcher;

  private String src =
    "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";
  private ByteArrayInputStream input = new ByteArrayInputStream(Base64.getDecoder().decode(src));
  private BufferedImage srcImage = ImageIO.read(input);

  public AzurePhotoFetcherTest() throws IOException {}

  /**
   * Helper-method to generate a stub for an UUID.
   *
   * @return stub for an UUID-class
   */
  private UUID stubForUuid() {
    return eq(UUID.randomUUID());
  }

  @Test
  public void getPhotoForUserId_shouldResizeAndConvertImageCorrectly()
    throws IOException, AzureUserFetchException {
    when(azureApiCaller.callApi(anyString(), any()))
      .thenReturn(new ByteArrayInputStream(Base64.getDecoder().decode(src)));

    String photoForUserId = azurePhotoFetcher.getPhotoForUserId(anyString(), stubForUuid());
    BufferedImage actualImage =
      ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(photoForUserId)));

    assertEquals(actualImage.getWidth(), 240);
    assertEquals(actualImage.getHeight(), 240);
  }

  @Test
  public void getPhotoForUserId_shouldReturnNullForEmptyResponse()
    throws IOException, AzureUserFetchException {
    when(azureApiCaller.callApi(anyString(), any())).thenThrow(IllegalStateException.class);

    String photoForUserId = azurePhotoFetcher.getPhotoForUserId(anyString(), stubForUuid());

    assertNull(photoForUserId);
  }
}
