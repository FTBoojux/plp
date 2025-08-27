package org.example.web.request;

import org.example.web.utils.io.BoundarySplitter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class KmpTest {
    public static void main(String[] args) throws IOException {
//        splitGeneral();
        splitWithBoundary();
    }
    private static void splitGeneral() throws IOException {
        String input = "----boAA----boBB----bocc----bod";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        List<byte[]> bytes = BoundarySplitter.splitStreamIntoByteArray(byteArrayInputStream, "--bo".getBytes(StandardCharsets.UTF_8));
        for (byte[] aByte : bytes) {
            System.out.println("|||");
            System.out.println(new String(aByte));
        }
    }

    private static void splitWithBoundary() throws IOException {
        String input = """
                ----------------------------754528313499813692769413
                Content-Disposition: form-data; name="username"
                
                
                ----------------------------754528313499813692769413
                Content-Disposition: form-data; name="file"; filename=""
                
                
                ----------------------------754528313499813692769413
                Content-Disposition: form-data; name="file"; filename="2df5e0fe9925bc310c1c31ac18df8db1cb137044.jpg"
                Content-Type: image/jpeg
                
                ���� JFIF  H H  �� C	
                ���e@pecnO�a��S����W��@e{X��[�T���
                �	��Y�x�Fq��0-{st�̔w5�'sh�G�L){ze7#~�#�+����,������o`��'{�/.i�����LdŖim�A�:G���c�C-P�M�Iە�پ_�-C}5m<U'8���$��Yy�/�:���Gq3��]ɿ�+�)-L�m(���G�7�t�R��4�Ǳ
                <Mf�8���+�1�
                =,�ٮ���H	 /�jeRL�9�~�*�w�:�0��+�>&����P�������%<�'�fR���A�Ю�};������u	�0=�Flz$��4��]c�PW#�
                v��J�%kv��S7���wV8BK1��u�b� s]뾩�J2A���4��s���?�Z������L��l��6iNĽ��G^�U/
                ��<@ɸ1��#�Pm�w+石�\\É��E0��t���H~[�}�H�����݊�ǖ�T�BBBBBBBBBBBy���LT�`{�.2��I|�����d<�Z7-c�����m8���)�G��G��/)�	6r���&�s��n�!��Zێ�&�'�?�J���Ѹ
                �R�E�m�s�D���gN���%��W��:9��?%������k��)Y���m�.W��7{�SE�I���;,��s
                �˝�0����;��|�K;���3q�{���?.!�MCS�6W�
                0f��p�7��	pH�!�%ԼY�M�FL!�|��,j��kG�n�ɣTf��<�L��
                /�4r�  �@��r���_U!d/CBBBBBBBBBW�vJ)�{Z7��XY~#���F]yi�t��>Xď���:��S�:�43��IW� r��t���K�]"h�Pѩ=Wu�jL��������#|��|Ez.I��a~������_-xGUdh���T���/3֕�e3[u[�X�ʴB�]I��b����[�v�-��)�B@ދ](��)�hH!1�� �O�0�2����ij��!�<"���r,��u��
                r32�[�*'3��a+.�;�V/LN�]��I�!gTFo�
                ���i嘕?
                W�U��r����n2�r�2�u��xW�8�=�
                ��dc1�g�/
                ��qY2ipv�f�h�T�
                �7w�/g����B���k�|gg
                �Y}l��8�@���e��\\�7�g�g<�d�XčvvA
                4�"v/���x����j>�G��ZHΗ�P�\\7�UϨlr��h��绢�:$��~�v�o�>�=�����J\\V)�����N�䶝�x(���1cߒ���x�qC��Yr���gn.���$Ͼ� §kn��"��f�q�n��u��'3�S��G�9Bϝ�c��9W�UӺL\\��5�g$��u-<-��zX,j
                �=��6��͙X���)��b�%ӄJ˸+�U pR�����!t�U)�!u�U�71�+#dy[u��V��*�m��MÕ�V�-�HtRF������%��r��l�j1��d״�Bl'�X �jVlQs�kT���1���n�F7Z4���<�j����xA�7�j1�^�N�`2h��7e]��,h5�Q��2��^G#��Se���-��r���ˌ�>�oԩ��ئp-�$��
                &��o�P>���Ǫ�;�Vh�@�J���| �! �! �! �! �! �! �! �! �! �?������7]b��Ty�������˚o
                ���P�e���<==���>OT���P�/C�� � � � � � � � � � �:~(e�+A��٬A��9�����+�>=X:���`�T�ʠi
                sy]p�3��HHSL���2k'�GtU
                ���+�r���y��7�'7�$ySΗ����	T�ӆ�����Tf;|(\\�WcE3�HX�Z��BB�N����#=E|�*�}����I��H��B*W�#�h�wSTO�j�ˊ�̒��q�Y�s��X���.�ܾ5U%uq���>ފ�YY@���{�YK]c��'m<��v�c�B�߳����E%C�YH\\N�gGe��#���;'���+��c�V�a�\\n���Q��qJɡd��3���"��!dc^$���V���bn�w�@� ǩ�?�>�}\\t�1���x7�Ucx���O���z5�����I�,U�ͦo,�����s���Ϟ{z���{�*�[�����E��?+4�헆ׅ�'f%OGŴs4�;:�U��U$2�9[g�+���x�%@v�H��Eװ�Nʘ�����sN�}��ٵ
                )��e�M��%$�̳域>o`�+�U������>gXl�� 5;�)!��~��@� ��� ���W�[(^}����7� 0��|��/��'5��M�cZ�uܤc���&Yz痎���[-8�9n��̶b<�R�q�z� �ύy!�q)tє����U|�ω٘��B�?x��!��5[������u��W>L=�h�!uBBBBBBB�����OS3"����� �P��?��W�G:f:�a�p^"�Q��`��ᡨ�����G��jMM]K�q��\\�����w�B��=>ݡ7w��A�pUUrL�>I�$�=�P���ê�����m�]��C^�K�W�F8�?5�rˮm�h�Z���.�G��=�^J�k��z�k�!�SK�~@���
                5T�6jy�,N��7L�9�!CSWOG	��vC��p�*��i!3O+#�����y�=��ML���A�����3�7+ͱ\\Ʀt��RJ:37+}�X��MㅯM�~�(�3Å�jeۈ��+�1\\���[U#�3�~[��6�����}W;��]��cuy�fiܞ�j `f׹�p!���[B�g4s ��ȳ\\���J�W���Х�m�8-o�h��ѧ�F��=�V&�K�`5�����}��e<�V��
                �a��a���b��wWs�^�N$]�}��ni��-]��
                � �m����gj�K�|a�=�
                ��6�7^���I`�����u�,�ځng+�wPbs����Q�7D����� ��
                �S8�6�+���b{�'����+�pn��񁟇�4��U,W���:B��W���g.q']\\���}���'�M#��&k��*����(���r3uqNg�D�y-٭���z�U�>K巢і@4c�\\λ��e>3V5u�E�a�>�u,�곟���vV�3�|G��'#��,� ~T�h���F��6B��(�kЦ�I	��oh��P����Y`.�.bhi��._�b�O���A#��l�9v�?�v3���Wí�iϣ���������x�!ۢ}N]H�f�pcwp���,��]m.t¾�p,R�<�u��)t#��B�@��;D.�ec�$D�l����!��V�%�c�L�8#�CI/=�V�|'��E������������E�`������g�M�Pp�#l����h6&��_;�c�s��?�&5A��3:�K������}��Э����UM/s�Q�F���vY�8�����L$ɂH�Av���G5~]:�X�Ga�\\<f{��]�N�<ὒ�_3�R��c��Y[���.y �P؍�����^??�S���Pcc�X��yuYм=�ʺ:���]�1he��Z�?,���c���IM�k����;G?������M�r�x�p!�cm֖��8d�����{���8`|\\�H�d����_u.2�3��o���YL�Yaw����NS/
                :��: ����q��GX;�{ms]�o��8�9^�*���������UO�H�q��>7�2����a���H�,}U��=��1�a� 5����WMr�6Q;�⸚���\\ī�S�V��c��<#.��E�S�*&���cl�+��[nT.sC|��8�����v!P�20�P�o�s�TI#���wB��i*�֟�Td���]
                �A��0芭;�U�6d�k���Q��|�����j���V�(�[dz ��rd���<h���s�ۗ������:3́'���F3Cdn�B�y[v��j�8���O�Vys䛌���22��6�]Ƈ�9��7@��S��N�r�=��� 2���/UC���;�:�K^��o̚ʷ��
                =Y*NnM��t��T�f.)�y��˪�In�3m�U�s˟A��[��&7��鲯OL��{�.��28L�a�R�N<wv}yCEy�L�GOF7�R:˛ج�+l�Y03��e ���]k��PJF�L��T�u͐��8���9�Y(ݶ*x��n�x�R:>��Y��wf���� �<~�7{�����n�h�'q�{usA�wNl��F�>�Q@�u�L�V�|�3�ߢ��=P���d�u!a#R��F��JF�9	�GY�� z�����R��;���5G�*��q�Q��r�`��������n�D�T�Ꞅح ��D����N�Q�dT��uT*#��{uW��j��5�b��z�,\\��#��X�#����z�W�jc+N_8�,V [�z-G�9��<����o���{���}
                T������Ϲ������Ϧ��X�|��ء1��AB�֭�u�X��9Vy�v]E�S��E	�f`��E��좔d��U~f�
                �E�n
                M;�~�1|yN���E+�̒��H�e�75�!�t��I�$��!��Cd���ws!{��������Ա��L4��M�~!o�l����db�
                wۺӇ�V6�$���w�K��ۂ0J���z&d-�G��z1vCgX��km	��c�9x^S�3A�e��>���ߕ�##��r��� GV~d�~�m�ۨ�<�"�r,���ڧ��6��n]Q�s�l��W}����c�Y�ŭm�M�V�8��,3���O��m³�{�G��
                ��*�����3�+�e�
                �FJ��cg�;�ḿ�UY�P2^���q���tW�����e�;�;��f9[�]j0
                H������Ep����w�4���Y���'����36V^ڛzh�}2vY�J������o(Z�/�(�*�P}҉��;W{�y�����B��D��F�T�=���(�3���q�La.~�u��v��J��?�9�Uq���s���F��v�r�Q	��'~#�c�b[��0̐S�� �=�z+4�-MC��9\\����䴸z����f@�&�g����~�Z<>��$�3�n�8�sÒ�;�
                c�L:�!����e.˜׷ծ�,}��1��Sİj�l/�uZ	���0�m�§2��W�b�c�{�cX;����EOj�]?�ެ0!J#�t-�i�܏���I�3y�y-�v��S�!T%�t%Bw9�UV�;��b�\s
                ����S��g��u����>�[�\\vpǄD��f!�dK��s^9o���e�l��Ĩx�9�lf�fl�縳�����oD� ����[�u��׺>ݨc�
                ���ׅ㛭�%�׉��#�s��:�蒙�aZ�"&J�R�3SR�e3+ٖK�R;�*�\\��9Q��⊾P�{��1�V��ZӐ�X�[�s�=������PHǉY}�'92y�JI�S���v�A����߫��}�q�CP�:���)�#\\�:nT��5g�D�:��#�˅F��۰���b���ۇ���;�9����X;N�)��s�d�L���X���2�U�QAUiX��`�ͱ-:�oP�v��#(��\\F���/�`ОV���i���,�B�&1I�w�,{)Ǖ����0�t3�7�'M�S��ķn��O��[�\\����ʼmk]�_E��T\\љ��,���m���:��&�����9�.C��z&���oD��l��s�rc�2�u�W~c��O�40���$����/����PFc��o�i)%�&8���y�����١c,����Cba�`�����D�`���3U�iLwX���8�n*�<'�*���t!�V�6�9���t����{��U�Yg�orxZ����ߊ�F���|���ܬ9)�E}�E��pOJa����I&�cϓ�m����!V�a�/�#���g�*�� <w��X2�e�i��+qI��J�tx�B��hAg*S��vC��Kr�Mek�fZ鱳�KϰA0nT�j�\\��*�:��GMT����dZ��!
                
                �a�M�zm�E���*D۔\\��V���8��2*�9�[��U�{��f9[�Fk�H'�_�%�-�)O�� �1�:@[�Q�������0�������� }�ouR�3F�w�T��r�SU	;vy�=��c�Co�ũ2�Hݎ�l�>�^�(7!�0�Q�/SJ8��.s��w�� �}n��!a�o�{���0����t�H� �[0=4�Z�)�1
                =(���6W�˕��J�T�fynz�/�%,���A>K5���kvOvʆ;̢�������'�r�E<)Iu�����-�~J]3���h9�sk�b�EG�ڮ����B���-�ȅ�7�t��q*߼�I)���sP��tor��pL,���9`�q%�?�^�Ɣt��R�T�㍶hՌܮ�r������u,�;+��p7�P�o��4U����$�,؞Mͻ��j1!;���8�J����E�N�ʯ�LW
                84u��;���C�v��u����]�1�o p�N'�v�^��tѺ�9�c_<q��.�9�l K���
                ��)ke>�v���v����f��Z��� 7t�ȽNR���]EMk�� ��0��Z�nR.�qcq��f#N�P�U�E o	גv|�X�4�����u8C�N�Y�J�%k�am�u�;I�ٮ��%�>W~�PP�S�c��B���ԝP�XTQ�b��N�!	H����Uek*7��d����~T!@�ܖ;�Z���<�Xl;�(%�����{vOc�D!P���ӹ�ޫ&�޳-�z!Q~c+Z$�ܾf�
                ��:�UO/��d!u�������i�6�}M��U]KI��a��Ki��;,������@x���띆�!�㷢�k���'�6�U��	vV�(���C#�l�OT!h<4fu���B�n�	�kL�nIvT8��R��:��%�)BEO�+���0���@l�Ӓ�n�8,�wĕ�!|ydGL�Ȣ��X�m{���&�c"��M<"��$��B�I�ف��6���c��\\�.˔�� v!	M+4�o���J�V��+�lw���B
                D��T!
                #��
                ----------------------------754528313499813692769413--""";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        List<byte[]> bytes = BoundarySplitter.splitStreamIntoByteArray(byteArrayInputStream, "----------------------------754528313499813692769413".getBytes(StandardCharsets.UTF_8));
        for (byte[] aByte : bytes) {
            System.out.println("---|||---");
            System.out.println(new String(aByte));
        }
    }
}
