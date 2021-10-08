package com.inforsecure.fiuapi.gateway.resource;

import lombok.Data;

@Data
public class DecryptFIDataResource {
    public String base64Data;
    public String base64RemoteNonce;
    public String base64YourNonce;
    public String ourPrivateKey;
    public KeyMaterialResource remoteKeyMaterial;
}
