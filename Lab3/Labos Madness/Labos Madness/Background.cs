using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Bday
{
    public class Background : Sprite
    {
        public Background(Texture2D spriteTexture, int width, int height)
        : base(spriteTexture, width, height)
        {
        }

        public override void Draw(SpriteBatch spriteBatch, Color Color)
        {
            spriteBatch.Draw(Texture, Position, Size, Color * 0.5f);
        }
    }
}
