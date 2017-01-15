using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Bday
{
    public class Bag : Sprite
    {
        /// <summary>
        /// Initial paddle speed. Constant
        /// </summary>
        private const float InitialSpeed = 0.6f;
        /// <summary>
        /// Paddle height. Constant
        /// </summary>
        private const int BagHeight = 236;
        /// <summary>
        /// Paddle width. Constant
        /// </summary>
        private const int BagWidth = 236;

        /// <summary>
        /// Current paddle speed in time
        /// </summary>
        public float Speed { get; set; }

        public Bag(Texture2D spriteTexture)
            : base(spriteTexture, BagWidth, BagHeight)
        {
            Speed = InitialSpeed;
        }

        public override void Draw(SpriteBatch spriteBatch, Color Color)
        {
            spriteBatch.Draw(Texture, Position, Size, Color);
        }
    }
}
